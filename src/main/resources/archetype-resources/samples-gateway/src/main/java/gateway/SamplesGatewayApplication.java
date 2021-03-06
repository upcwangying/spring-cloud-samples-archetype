#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 *
 * MIT License
 *
 * Copyright (c) 2019 cloud.upcwangying.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ${package}.gateway;

import ${package}.gateway.config.Swagger2Properties;
import ${package}.gateway.filters.RateLimitFilterByCpu;
import ${package}.gateway.filters.RateLimitFilterByIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;

import java.time.Duration;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(Swagger2Properties.class)
public class SamplesGatewayApplication {

    @Autowired
    private RateLimitFilterByCpu rateLimitFilterByCpu;

    public static void main(String[] args) {
        SpringApplication.run(SamplesGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator rateLimit(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/rateLimitByIp")
                        .filters(f -> f.filter(new RateLimitFilterByIp(10, 1, Duration.ofSeconds(1))))
                        .uri("lb://product")
                        .id("rateLimitByIp_route")
                )
                .route(r -> r.path("/rateLimitByCpu")
                        .filters(f -> f.filter(rateLimitFilterByCpu))
                        .uri("lb://product")
                        .id("rateLimitByCpu_route"))
                .build();
    }

//    @Bean
//    public ServerCodecConfigurer serverCodecConfigurer() {
//        return ServerCodecConfigurer.create();
//    }
}
