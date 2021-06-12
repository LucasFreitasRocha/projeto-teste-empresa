# projeto-nativa

   Esse repositorio é  uma api e um desafio logico para um processo seletivo desenvolvido com framework spring boot (JAVA), foi implementado spring security para autenticação com jwt (Bearer token).
   Para rodar a aplicação é só escolher a ide (intellij, sts , eclipse e etc), o spring boot já tem o servidor tomcat embutido e por padrão o servidor é levantado na porta 8080
   http://localhost:8080/
   
## Swagger

   para facilitar a interação com a api foi incluido o swagger na aplicação. O Swagger é uma linguagem de descrição de interface para descrever APIs RESTful expressas usando JSON.
   O swagger é acessível na rota /swagger-ui.html - http://localhost:8080/swagger-ui.html#/

![alt text](https://github.com/LucasFreitasRocha/projeto-nativa/blob/master/img/Swagger.PNG)

## Autorização
   A maioria das rotas são protegidas e para acessar é necessario adicionar um token no header "Bearer token" pois a api não está guardando estado(sessão)
   
   ![alt text](https://github.com/LucasFreitasRocha/projeto-nativa/blob/master/img/headerToken.png)

   Mas tem rotas que não precisa está auntenticado, para configurar foi criado uma classe SecurityConfiguration  no package com.nativa.settings 
   
   ![alt text](https://github.com/LucasFreitasRocha/projeto-nativa/blob/master/img/rotasprotegidas.png)
   
   Rotas que estão livre:
   - metodo post /user:  que é a rota de criação de usuario
   - metodo post /auth: para se autenticar
   - metodo get /desafio-logico é a rota que é feito o desafio logico
   
   para configurar rota livre como o modulo é só utilizar o metodo permitALL passando o a rota e o metodo
    no antMatchers como está na foto,  (ex: http.authorizeRequests().antMatchers(HttpMethod.POST, "/user").permitAll())
    
   Para os outras rotas é necessario o token como foi explicado anteriomente, e para validar o token foi criado middleware(filter) -  AutenticacaoViaTokenFilter:
   ![alt text](https://github.com/LucasFreitasRocha/projeto-nativa/blob/master/img/middleware.png)


## Autenticação
   Para se autenticar é enviado um post para rota /atuh com body contendo email e senha
  ![alt text](https://github.com/LucasFreitasRocha/projeto-nativa/blob/master/img/auth.png)
  