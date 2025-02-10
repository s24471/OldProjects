# 📰 My RSS News App
My RSS News App is a mobile application for browsing news from RSS feeds. It allows users to follow the latest updates, save favorite articles, tracks the history of articles read, and receive notifications about new content. I created this app for my uni classes.

# Features
-  RSS Feed Parsing – Fetch and display news from a selected RSS source.
-  User Authentication – Register and log in using email or Google Sign-In.
-  Favorites & Read Tracking – Mark articles as read or add them to favorites.
-  Notifications – Get automatic alerts about new articles via WorkManager.
-  WebView Integration – Open articles directly in the app.
- Firestore – Storing user account data, favorite articles, and read history.

# Tech Stack
- Kotlin – Core programming language
- Firebase Authentication – User login and registration
- Firestore – Storing read and favorite articles
- Jsoup – Parsing RSS content
- WorkManager – Background task scheduling for news updates
- RecyclerView – Efficiently displaying news articles
- Picasso – Loading and caching images