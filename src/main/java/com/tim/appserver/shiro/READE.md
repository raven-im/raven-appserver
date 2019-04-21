### Shiro介绍

-------

* Subject：主体，代表了当前“用户”，这个用户不一定是一个具体的人，与当前应用交互的任何东西都是Subject，
如网络爬虫，机器人等；即一个抽象概念,Subject包含的信息很多，主要有用户名、权限、角色、
Shiro会将Subject保存到Session中，下次访问时根据cookie中的sessionId拿到session并且获取其中的Subject

* shiroFilter: 安全控制的入口点，判断访问当前URL需要登录/权限等工作,使用最多的控制策略为以下三种
    * `anon` 匿名访问
    * `authc` 认证访问，每次访问都进行认证和授权
    * `userBean` rememberMe访问 
    * 还以通过具体角色与权限进行过滤

* RememberMe: 用户通过记住我登录成功后，会将principal(我们理解为用户名)通过加密保存到cookie中,
用户下次访问时不会进行认证而是直接进行授权
    
* AuthenticationToken: 用来认证和授权的token，一般包含用户名、密码、是否记住我

* Realm:  安全数据源，Shiro从Realm获取安全数据(如用户、角色、权限),获取安全数据的代码需要开发者自行实现，
将实现的Realm子类交由Shiro管理，如果有多种安全数据源，可以通过继承实现多个AuthenticationToken子类，
在Realm中判断传入的Token是否进行验证和授权，一个token可以由多个Realm进行认证

* AuthenticationInfo: 根据AuthenticationToken进行用户认证，认证成功会生成principal

* AuthorizationInfo: 根据principal获取用户授权信息，一般包含角色、权限

* RealmAuthenticator: Realm的认证策略，一般设置成至少一个Realm认证成功

* SessionManager：管理Session的生命周期,会将sessionId设置到cookie当中

* SessionDAO：Session的CRUD，注入给SessionManager

* CacheManager：缓存控制器，来管理如用户、角色、权限等的缓存的

* SecurityManager：相当于SpringMVC中的DispatcherServlet或者Struts2中的FilterDispatcher；
是Shiro的心脏；所有具体的交互都通过SecurityManager进行控制；它管理着所有Subject、Realm、SessionManager、
CacheManager

### 基于角色的访问控制

-------


RBAC核心思想就是将访问权限与角色相关联，通过给给用户分配合适的角色，让用户与访问权限相联系，
常用的RBAC有以下几种模型。

* RBAC0: 基本模型，用户与角色多对多，角色与权限多对多

* RBAC1: 在RBAC0的基础上引进角色继承的概念，子角色继承父角色的所有权限

* RBAC2: 在RBAC0的基础上引进了以下概念

    * 互斥: 同一用户仅可分配到一互斥角色集合中至多一个角色，这样支持了职责分离的原则，另外权限也可以互斥
    
    * 基数限制：一个用户可拥有的角色数量受限，一个角色可拥有的权限数量有限

    * 先决条件：当用户拥有了A角色时才可以分配给用户B角色，权限同理

    * 时间频度限制：规定特定角色权限的使用时间和频率

    * 运行时互斥：例如允许一个用户两个角色的成员资格，但在运行时不同同时的激活这两个角色

* RBAC3：RBAC1和RBAC2的结合



