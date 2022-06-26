package com.unzer.payments.config;

//@Configuration
//@EnableWebSecurity
//public class ApplicationSercurityConfig extends WebSecurityConfigurerAdapter {

//    @SuppressWarnings("deprecation")
//    @Bean
//    public static NoOpPasswordEncoder passwordEncoder() {
//        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/api/v1/payments/*/create")
//                .hasRole("USER")
//                .antMatchers("/api/v1/payments/*/cancel")
//                .hasRole("ADMIN")
//                .antMatchers("/**").hasRole("ADMIN")
//                .and()
//                .formLogin();

//    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER")
//                .and()
//                .withUser("admin").password("admin").roles("ADMIN");
//    }

//}
