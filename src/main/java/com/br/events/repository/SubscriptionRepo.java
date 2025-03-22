package com.br.events.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.br.events.dto.SubscriptionRankingItem;
import com.br.events.model.Event;
import com.br.events.model.Subscription;
import com.br.events.model.User;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer>{
	
	public Subscription findByEventAndSubscriber(Event evt, User user);

	@Query(value="SELECT COUNT(subscription_number) AS qtde, indication_user_id, "
			+ "FROM tbl_subscription INNER JOIN tbl_user "
			+ "ON tbl_subscription .indication_user_id = tbl_user.user_id "
			+ "WHERE indication_user_id IS NOT NULL "
			+ "AND event_id = :evenId "
			+ "GROUP BY indication_user_id "
			+ "ORDER BY quantidade DESC", nativeQuery=true)
	public List<SubscriptionRankingItem> generateRanking(@Param("eventId") Integer eventId);
}
