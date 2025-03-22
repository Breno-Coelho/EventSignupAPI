package com.br.events.service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.events.dto.SubscriptionRankingByUser;
import com.br.events.dto.SubscriptionRankingItem;
import com.br.events.exception.EventNotFoundException;
import com.br.events.exception.SubscriptionConflictException;
import com.br.events.exception.UserIndicatorNotFoundException;
import com.br.events.model.Event;
import com.br.events.model.Subscription;
import com.br.events.model.User;
import com.br.events.repository.EventRepo;
import com.br.events.repository.SubscriptionRepo;
import com.br.events.repository.UserRepo;

@Service
public class SubscriptionService {
	
	@Autowired
	private EventRepo evtRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private SubscriptionRepo subRepo;
	
	public Subscription CreateNewSubscription(String eventName, User user, Integer userId) {
		
		Event evt = evtRepo.findByPrettyName(eventName);
		if (evt == null) {			
			throw new EventNotFoundException("The event "+eventName+" was not found");
		}
		
		User userRec = userRepo.findByEmail(user.getEmail());
		if (userRec == null) {			
			userRec = userRepo.save(user);
		}
		User indicator = null;
		if (userId != null) {
			indicator = userRepo.findById(userId).orElse(null);
			if (indicator == null) {
				throw new UserIndicatorNotFoundException("User indicator "+userId+" not found");
			}
		}
		
		Subscription subs = new Subscription();
		subs.setEvent(evt);
		subs.setSubscriber(userRec);
		subs.setIndication(indicator);
		
		Subscription tmpSub = subRepo.findByEventAndSubscriber(evt, userRec);
		if (tmpSub != null) {
			throw new SubscriptionConflictException("Already exists some inscription for the user "+userRec.getName());
		}
		
		return subRepo.save(subs);
		 
	}
	

	public List<SubscriptionRankingItem> getCompleteRanking(String prettyName){
		Event evt = evtRepo.findByPrettyName(prettyName);
		if (evt == null) {
			throw new EventNotFoundException("Ranking of event "+prettyName+" was not found");
		}
		return subRepo.generateRanking(evt.getEventId());
	}
	
	public SubscriptionRankingByUser getRankingByUser(String prettyName, Integer userId) {
		List<SubscriptionRankingItem> ranking = getCompleteRanking(prettyName);
		
		SubscriptionRankingItem item = ranking.stream().filter(x->x.userId().equals(userId)).findFirst().orElse(null);
		
		if (item == null) {
			throw new UserIndicatorNotFoundException("The selected user has no indications");
		}
		Integer position = IntStream.range(0, ranking.size()).filter(pos -> ranking.get(pos).userId().equals(userId)).findFirst().getAsInt();
	
		return new SubscriptionRankingByUser(item, position+1);
	}
}
