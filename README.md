## Security와 관련 용어

#### 접근주체(Principal)
* 보호된 대상에 접근하는 유저(User)

#### 인증(Authenticate)
* 접속하는 유저가 누구인가 확인(로그인 절차)

#### 인가(Autherize)
* 접근한 유저가 어떤 서비스, 어떤 페이지에 접근할 수 잇는 권한이 있는가

#### 권한(Role)
* 인증(Authenticate)된 주체(User)가 어떤 페이지, 기능, 서비스에 접근할 수 있는 권한이 있다 는 것을 보증, 증명

#### 무결성, 보안
* 무결점 : 인가된 사용자에 의해 손상될 수 있는 것들
* 보안 : 인가되지 않은 사용자에 의해 손상될 수 있는 것들

## Spring Security
* Filter를 사용하여 접근하는 사용자의 인증절차와 인가를통해 권한이
있는가를 파악하고, 적절한 조치(되돌릭, Redirect, 사용가능)를 비교적 적은
코드 양으로 처리할 수 있도록 만든 framework

* Spring security는 Session과 쿠키방식을 병행하여 사용한다.

### 유저가 로그인을 시도하면
1. Authentication Filter에서부터 users table까지 접근하여 사용자 정보를 인증하는 절차를 거친다.

2. 인증이 되면 user table, user detail table에서 사용자 정보를 fetch(=select)하여 session에 저장

3. 일반적인 HttpSession은 서버의 활동 영역 메모리에 session을 저장하는데비해
Spring Security는 "SecurityContextHolder"라는 메모리에 저장

4. View로 유저의 정보가 담긴 session을 session Id와 함게 응답으로 전달
>* JSESSIONID라는 쿠키에 SESSION ID를 담아서 보내고

5. 이후 유저가 접근을 하면 JSESSIONID에서 쿠키를 추출하여 사용자 인증을 시도한다.
*  ?JSESSIONID=asjidfjwk 이러한 값이 URL뒤에 따라 붙기도 한다.

6. JSessionId에서 추출한 Session Id가 유효하면 접근 Request에게 Authentication을 부착한다.

### Spring-Security와 form데이터
* web browser에서 서버로 요청하는 것을 request라고 하며,
요청할 때 사용하는 주소를 URL, URI라고 한다.
* web browser에서 서버에 request하는 method 방식에는 GET, POST, PUT, DELETE가 있고,
이중 SpringMVC에서는 GET, POST를 주로 사용한다.

* GET method는 주소창에 URL을 입력하고 Enter를 누르거나, anchor tag를 마우스로 클릭하거나
또는 form tag의 method가 없는 경우 서버로 요청하는 방식이다.

* GET method는 단순히 리스트를 요구하거나, 입력 form 화면을 요구하는 용도로 주로 사용된다.
* POST method는 입력화면에 값들을 입력한 후 서버로 전송할 떄 주로 사용하며,
입력화면의 form, input 등의 tag에 값을 저장한 후 server로 submit을 수행하는 경우이다.
* POST method는 데이터의 양에 관계없이 서버로 전송할 수 있으며 file upload 등도 수행할 수 있다.


### csrf Token  
* Spring-Security를 적용한 프로젝트에서 GET method방식은 아무런 제약이 없으나,
POST method방식은 서버로부터 전달받은 csrf Token을 데이터들과 함께 보내야만 정상적으로 서버로 보낼 수 있다.
* 그 때문에 POST방식의 form 코드에는 다음과 같은 코드를 추가해주어야한다.
(혹은 spring form tag로 form을 전송해주면 됨)
 				
	<input class="form-control" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

* csrf Token을 빠뜨리고 해당 페이지의 데이터를 전송하면 서버는 수신을 거부하고 403(forbidden)오류를 보낸다.

* 이러한 실수를 방지하기 위해 Spring form taglibs를 사용하여 form을 작성하는 대안이 있다.

	<form:form> ... <form:form>
	 
* 형식의 form화면을 작성하면 spring form tag libs는 자동으로 csrf Token을 form 화면코드에 추가하여
별도의 조치를 취하지 않아도 문제가 발생하지 않도록 만들어 준다.



* include-nav.jspf에서 logout할 때 spring formtag를 사용하지 않으면
따로 csrf token값을 같이 보내줘야 로그아웃이 수행된다.

			spring form tag를 안 쓸 경우 csrf token값을 같이 보내줘야함
			$.post("${rootPath}/logout", { ${_csrf.parameterName:"${_csrf.token"}, function(result){
				alert(result)
				document.location.replace("${rootPath}/")
			})
			
## E-mail 인증 회원가입
* 회원가입을 할 때 username, password, email을 입력받고 email을 사용자에게 보낸 후 email 인증을 수행하여
회원가입을 수행한다.

* 회원가입 화면을 username/ password를 입력받는 화면과, email을 입력받는 2개의 화면으로 분리
* SessionAttributes, ModelAttribute를 활용하여 구현
* SessionAttributes는 보통 vo 객체를 서버 메모리에 저장한 후 form 화면과 연동하는 구현으로 이때 반드시 
Model Attribute가 동반되어 구현되어야 한다.

* SessionAttributes에 등록된 ModelAttribute vo 객체는 서버 메모리에 데이터를 보관하고 있다가,
form:form을 통해서 서버로 전달되는 vo 객체를 받고, form:form에서 누락된 input 항목들이 있으면 메모리dp
보관된 ModelAttribute

## 화면을 2개로 분리해서 회원가입 수행하기
* 화면을 두개로 분리하는 코딩을 수행할때는
* 첫번째 화면의 데이터 + 두번째 화면의 데이터를 취합해 최종 화면에서 두 화면의 데이터를 모두 받아 작업을 수행해야 한다.
* 이럴경우에는 첫번째 화면에서 받은 데이터를 두번째 화면에서 hidden방식의 input tag로 작성해주어야하는 불편함이있다.
*
* 두 화면에서 입력된 내용이 모두 서버에 전달되어야 crud를 수행할 수 있음
* session/Model Attribute가 모든 데이터를 통합해서 관리해 하나의 vo에 두개의 데이터를 모두 모아서 사용할 수 있도록 관리해줌
* 이게 sessionAttribute를 사용하는 목적 
* jsp단에서는 spring form tag를 사용해 데이터를 


* SessionAttribute를 사용하면 Session(server 가상머신 storage에 저장함)
* 첫번째 화면에서 입력된 내용을 vo에 저장함
* 두번째 화면에서 입력된 내용을 vo에 추가해서 저장
* 최종화면에서는 화면들에서 보낸 데이터가 모두 통합되어 vo에 저장된 상황이 됨.
* 굳이 model에 데이터를 심지 않아도 userVO를 session에 등록하고 ModelAttribute Annotaion을 사용하면
* 반드시 spring form tag의 input으로 보내야 가능함
* 단점 만약 뒤로가기를 누르면 문제가 발생할 수 있음(그전에 보낸 데이터가 input box에 담겨있을 수 있음) 이부분에 대한 처리를 따로 수행해줘야함
* 세번재 화면에서 입력된 내용을 vo에 추가해서 저장
* 첫번째 화면에서는 


## Transactional
* MyBatis와 common-dbcp 환경에서는 context.xml에 <tx:annotation-driven /> 설정을 통해서 
자동으로 transaction을 구현할 수 있다.

* MyBatis 환경에서 실제 Dao Interface와 mapper.xml 등을 연동하여
DB와 query를 주고받을 때는 SqlSesionTemplate라는 클래스를 통해서 사용한다.

* DataSourceTransactionManager를 context.xml에 설정하게되면
SqlSessionTemplate를 사용하지 않아도 내부적으로 자체 처리가 된다.

* DataSourceTransactionManager가 SqlSessionTemplate 역할을 대신 수행하기도 한다.

* 여기에서 <tx:annotaion-driven /> 항목이 없고, class나 method에서 @Transactional 설정이 없으면
DataSourceTransaction은 SqlSessionTemplate와 같은 역할만 수행한다.

* 혹시 <tx:annotaion-driven/>을 설정했는데 @Transactional 설정된 method에서 transaction이 적용이 안될때가 있는데,
이때는 <tx:annotaion-driven /> 코드 위쪽에 <context:annotaion-config />를 설정해 주어야 한다.

## Transactional의 옵션
* @Transactional에는 특별히 세세하게 설정할 수 있는 옵션들이 있다.
* 아래 옵션들은 transactionl을 customizing 하지 않고 기본값으로 사용했을 때 사용가능하다.

#### isolation
* 현재 transaction이 작동되는 과정에서 다른 transaction등이 접근하는 정도를 설정하기

* READ_UNCMMITED : level 0

>* transaction 처리 중 또는 COMMT이 완료되기 전에 다른 트랜잭션이 읽기를 수행할 수 있다.
(ex : 성적처리 application은 transaction 처리 중 읽기만 수행할 수 있도록 이와같은 처리를 수행해 주어야 다른 사람들이 업무를 할 수 있음)

* READ_COMMITED : level 1

>* transaction이 commit 된 후에만 다른 transaction이 읽을 수 있다.
(ex : 은행로직 처리 application은 transaction처리 중 다른 일을 수행할 수 없게 commit 완료 전에는 lock을 걸어야 할 필요 있음)

* REPEATABLE_READ : level 2

>* transaction이 진행되는 동안에 select 문장이 사용된 table에 Lock을 걸기,
SELECT가 실행되거나 실행될 예정인 DB(Table)에는 CUD를 수행할 수 없도록 하며, 단 다른 transaction에서 제한적으로 SELECT가 가능하다.

>* 다수의 transaction이 SELECT를 수행할 때 일관된 무결성 있는 데이터를 가져갈 수 있도록 보장

* SEREALIZABLE : level 3

>* 완벽한 일관성 있는 SELECT를 보장

#### propagation : 전파옵션
* 현재 transaction이 시작되었음을 다른 transaction에 어떻게 알릴 것인가

* REQUIRED

>* 부모 transaction이 실행되는 과정에서 또 자식(세부적인) transaction을 실행할 수 있도록 허용
>* 이미 자식 transaction이 실횅되고 있으면 새로 생성 금지

* REQUIRED_NEW

>* REQUIRED와 비슷하지만 자식 TRANSACTION이 이미 실행되고 있어도 무조건 다시 생성하라 

#### READONLY
* 해당 transaction을 읽기 전용으로 설정하겠다. 기본값은 false

#### rollbackFor
* rollBack 조건을 무엇으로 하겠느냐. 특별히 어떤 예외가 발생했을때만 rollback 하게끔 설정할 수도 있음.
기본값은 Exception.class(모든 exception 발생 시)

#### noRollbackFor
* norollbackFor와 반대되는 개념으로, 특별한 예외에서는 rollback을 무시하라. 
기본값은 null

#### timeout
* DB와 연결하여, transaction이 실행되는 시간이 과도하게 진행될 경우,
rollback을 수행하도록 설정.
기본값은 -1(timeout rollback 금지)

### Transactional Annotaion 사용시 List insert, update 수행할 때 주의 사항!!!
* 서비스 코드에서 다음과 같은 코드의 사용은 절대 금지

		for(DataVO vo : dataList){
			Dao.insert(vo)
		}

* 선행되는 작업 수행 중 어떤 원인에 의하여 exception이 발생해 원하는 작업이 이루어지지 않으면
(connection pool에서 transactional을 실행시켜 모든 작업이 수행된 후 commit을 수행함)
선행작업에서 commit이 이루어지지 않아 후행되는 작업은 절대 수행되지 않는다.
따라서 Dead lock 등의 상황이 발생가능하며 아직 고쳐지지 않았다.

