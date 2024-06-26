package com.example.lunimary.util

inline val empty: String get() = ""

inline val unknownErrorMsg: String get() = "unknown error."

inline val Long.Companion.Default: Long get() = 0L

inline val Int.Companion.Default: Int get() = 0

inline val id: String get() = "id"

//Users
inline val userRootPath: String get() = "/users"
inline val getUserPath: String get() = "$userRootPath"
inline val updateUserPath: String get() = "$userRootPath"
inline val deleteUserPath: String get() = "$userRootPath"
inline val checkIsLoginPath: String get() = "$userRootPath/isLogin"
inline val loginPath: String get() = "$userRootPath/login"
inline val logoutPath: String get() = "$userRootPath/logout"
inline val registerPath: String get() = "$userRootPath/register"
// Routing
//Page
inline val pageRootPath: String get() = "/pages"
inline val pageArticlesPath: String get() = "$articlesRootPath$pageRootPath"
inline val pageFriendsArticlesPath: String get() = "$articlesRootPath$pageRootPath/friends"
inline val pageUsersPath: String get() = "$userRootPath$pageRootPath"
inline val pageCollectsPath: String get() = "$collectRootPath$pageRootPath"
inline val pageLikesPath: String get() = "$likeRootPath$pageRootPath"
inline val pageCommentsPath: String get() = "$commentRootPath$pageRootPath"

inline val pageMyFriendsPath: String get() = "$friendRootPath$pageRootPath/relation/my_friends"
inline val pageMyFollowingsPath: String get() = "$friendRootPath$pageRootPath/relation/followings"
inline val pageMyFollowersPath: String get() = "$friendRootPath$pageRootPath/relation/followers"
inline val pageRecommendedArticlesPath: String get() = "$articlesRootPath$pageRootPath/recommended"


//Article
inline val articlesRootPath: String get() = "/articles"
inline val createArticlePath: String get() = "$articlesRootPath/create"
inline val getArticleByIdPath: String get() = "$articlesRootPath"
inline val updateArticleByIdPath: String get() = "$articlesRootPath/update"
inline val deleteArticleByIdPath: String get() = "$articlesRootPath"
inline val likesOfUserPath: String get() = "$likeRootPath/likes_num"
inline val publicArticlesOfUserPath: String get() = "$articlesRootPath/user/public"
inline val privacyArticlesOfUserPath: String get() = "$articlesRootPath/user/privacy"
inline val getAllArticlesOfUserCollectedPath: String get() = "$articlesRootPath/user_collected"
inline val getAllArticlesOfUserLikedPath: String get() = "$articlesRootPath/user_liked"
inline val whenBrowseArticlePath: String get() = "$articlesRootPath/when_browse_article"
inline val interactionDataPath: String get() = "/users/interaction_data"
inline val viewArticleDurationPath: String get() = "$articlesRootPath/view/duration"
inline val isArticleDeletedPath: String get() = "$articlesRootPath/is_deleted"


//Like
inline val likeRootPath: String get() = "/likes"
inline val createLikePath: String get() = "$likeRootPath/create"
inline val getAllLikesOfUserPath: String get() = "$likeRootPath/user"
inline val getAllLikesOfArticlePath: String get() = "$likeRootPath/article"
inline val deleteLikePath: String get() = "$likeRootPath"
inline val cancelLikePath: String get() = "${likeRootPath}/cancel"
inline val existsLikePath: String get() = "${likeRootPath}/exists"

//Collect
inline val collectRootPath: String get() = "/collects"
inline val createCollectPath: String get() = "$collectRootPath/create"

inline val getAllCollectsOfArticlePath: String get() = "$collectRootPath/article"
inline val deleteCollectPath: String get() = "$collectRootPath"
inline val cancelCollectPath: String get() = "${collectRootPath}/cancel"
inline val existsCollectPath: String get() = "${collectRootPath}/exists"


//Comment
inline val commentRootPath: String get() = "/comments"
inline val createCommentPath: String get() = "$commentRootPath/create"
inline val getAllCommentsOfUserPath: String get() = "$commentRootPath/user"
inline val getAllCommentsOfArticlePath: String get() = "$commentRootPath/article"
inline val deleteCommentPath: String get() = "$commentRootPath"
//Friend
inline val friendRootPath: String get() = "/friends"
inline val followPath: String get() = "$friendRootPath/follow"
inline val unfollowPath: String get() = "$friendRootPath/unfollow"
inline val myFollowingsPath: String get() = "$friendRootPath/following"
inline val myFollowersPath: String get() = "$friendRootPath/followers"
inline val existingFriendshipPath: String get() = "$friendRootPath/existing_friendship"
inline val mutualFollowFriendsPath: String get() = "$friendRootPath/mutual"
inline val invisibleFollowPath: String get() = "$friendRootPath/invisible_follow"


//Token
inline val refreshToken: String get() = "/token/refresh"

//Online Status
inline val isForegroundStr: String get() = "isForeground"
inline val onlineStatusPath: String get() = "/online_status"

inline val fileDownloadPath: String get() = "/file/download"
inline val fileUploadPath: String get() = "/file/upload"


//Message
inline val messageRootPath: String get() = "/message"
inline val messageCommentPath: String get() = "$messageRootPath/comment"
inline val messageLikePath: String get() = "$messageRootPath/like"
inline val messageFollowPath: String get() = "$messageRootPath/follow"

//Search
inline val searchRootPath: String get() = "/search"
inline val searchArticlePath: String get() = "$searchRootPath/article"
inline val searchUserPath: String get() = "$searchRootPath/user"


//Topic
inline val topicRootPath: String get() = "/topic"
inline val userSelectedTopicsPath: String get() = "$topicRootPath/user_selected"
inline val createOrUpdateTopicsPath: String get() = "$topicRootPath/create_or_update"
inline val recommendTopicsPath: String get() = "$topicRootPath/recommend"

