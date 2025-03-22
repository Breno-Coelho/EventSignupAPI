# EventSignupAPI
The **EventSignupAPI** is a Java-based API designed to manage user registrations for events. This system allows users to subscribe to events, track their subscriptions, and receive confirmations, providing an efficient solution for event management.

## Features

- **User Registration:** Users can register in the system by providing their name and email.
- **Event Subscription:** Users can subscribe to available events.
- **Subscription Management:** Users can view and manage their subscriptions.
- **Event Information:** Detailed information about each event, including title, location, date, time, and price.
- **Scalable and Efficient:** Designed to handle a large number of users and events.

## Technologies Used

- **Java:** The core programming language used to develop the API.
- **MySQL:** Database used to store user, event, and subscription information.
- **GitHub:** Version control and project hosting.

## Functional Requirements

1. **Registration**:
    - The user can register for an event using their name and email.
2. **Referral Link Generation**:
    - The user can generate a referral link (one per registrant).
3. **Referral Ranking**:
    - The user can view the referral ranking.
4. **Referral Visualization**:
    - The user can see the number of registrants who joined using their referral link.

## User Stories

### US00 - Event CRUD

This User Story is necessary to support the existing User Stories and Functional Requirements.

Some functionalities to manage events:

- Creation of a new event
- Listing of all available events
- Retrieval of details for a specific event by ID
- Retrieval of details for a specific event by its Pretty Name

- ## Endpoint: `POST /events`
**Description:** Creates a new event.

### Response
```json
{
  "name": "Event Test 2025",
  "location": "Online",
  "price": 0.0,
  "startDate": "2025-03-16",
  "endDate": "2025-03-18",
  "startTime": "19:00:00",
  "endTime": "21:00:00"
}
```

- ## Endpoint: `GET /events`
**Description:** List all events.

### Response

```json
[
  {
    "id": 1,
    "name":"Event Test 2025",
    "prettyName":"event-test-2025",
    "location":"Online",
    "price":0.0,
    "startDate":"2025-03-16",
    "endDate":"2025-03-18",
    "startTime":"19:00:00"
    "endTime":"21:00:00"
  }
]
```

- ## Endpoint: `GET /events/PRETTY_NAME`
**Description:** Return a event by their Pretty Name

**Exemple:** 
http://localhost:8080/events/event-test-2025

**Response:**
```json
{
  "id": 1,
  "name":"Event Test 2025",
  "prettyName":"event-test-2025",
  "location":"Online",
  "price":0.0,
  "startDate":"2025-03-16",
  "endDate":"2025-03-18",
  "startTime":"19:00:00"
  "endTime":"21:00:00"
}
```

## Use Case

### Base Case

**Conditions:** Event does not exist in the database.

**Actions:**
- Generate the event's pretty name.
- Register the event in the database.
- Return the event with its ID.

### Alternative Case 1

**Conditions:** An event with the same pretty name exists in the database.

**Actions:**
- Generate the pretty name and retrieve the event with this pretty name.
- If the event exists, throw an `EventConflictException`.

### US01 - Register for an Event

Endpoint: POST /subscription/PRETTY_NAME

This User Story addresses the functional requirements RF01 and RF02.

- The user can register for a previously registered event by providing their name and email.
- Since this is a system where we can have multiple events, it is possible that a user is already in our database from participating in previous events. In this case, simply retrieve their data and proceed with the registration.
- The user cannot register twice for the same event. If there is already a registration for the respective event by the user, an error message should be returned (conflict).
- At the end of the registration, the response will be a JSON with the registration number for the event.

**Expected Request**
```json
{
   "userName":"John Doe",
   "email":"john@doe.com"
}
```

**Expected Response**
```json
{ 
  "subscriptionNumber":1,
  "designation": "https://localhost:8080/event-test-2025/123"
}
```

### Use Cases

**Base Case:**

**Conditions:** Previously registered event, User does not exist (email not found).

**Actions:**
- Insert the user into the database.
- Add a new registration for the user.
- Return the registration result containing the ID and the referral link.

**Alternative Case 1:**

**Conditions:** User exists in the database but has no registration.

**Actions:**
- Add a new registration for the user.
- Return the registration result containing the ID and the referral link.

**Alternative Case 2:**

**Conditions:** Event does not exist.

**Actions:**
- Throw an `EventNotFoundException` indicating that the event does not exist.

**Alternative Case 3:**

**Conditions:** User already has a registration for the event.

**Actions:**
- Throw an exception indicating a conflict.

**Alternative Case 4:**

**Conditions:** User registers through an invitation from an existing user (the inviting user exists in the database).

**Actions:**
- Register a subscription based on the referral from a user.

**Alternative Case 5:**

**Conditions:** The inviting user does not exist in the database.

**Actions:**
- If the user does not exist, generate the registration without a referral.

### US02 - Generate Referral Ranking

```bash
Endpoint: GET /subscription/PRETT_NAME/ranking
```

This User Story addresses the functional requirement RF03.

- Possibility to generate a ranking of the number of registrations by referral (i.e., ordered by the sum of registrations by referral).
- Ideal: the ranking displays the top 3 (gold, silver, and bronze).

```bash
http://localhost:8080/subscription/event-test-2025/ranking
```

```json
[
  {
    "userName":"John Doe",
    "subscribers":1000
  },
  {
    "userName":"Mary Page",
    "subscribers":873	
  },
  {
    "userName":"Frank Lynn",
    "subscribers":690	
  }
]
```

### US03 - Generate Statistics of Registrations by Participant

```bash
Endpoint: GET /subscription/PRETTY_NAME/ranking/USERID
```

This User Story addresses the functional requirement RF04.

- Retrieve the number of registrants who effectively participated in the event referred by a specific user (USERID), as well as their position in the overall ranking.

```bash
http://localhost:8080/subscription/event-test-2025/ranking/123
```

```json
{
  "rankingPosition":3,
  {
    "userId":123,
    "name":"John Doe",
    "count":600
  }
}
```

## Integrated Tests

- Register Event (`/events`)
    - Success: 201
    - Failure: `EventConflictException`: 409
- Find Event by Pretty Name
    - Success: 200
    - Failure: EventNotFound 404









