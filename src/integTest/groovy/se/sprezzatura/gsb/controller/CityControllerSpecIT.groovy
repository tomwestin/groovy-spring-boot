package se.sprezzatura.gsb.controller

import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import se.sprezzatura.gsb.Application
import spock.lang.Shared
import spock.lang.Specification

@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
@WebAppConfiguration
@IntegrationTest
class CityControllerSpecIT extends Specification {
    @Shared
    def endpoint = new RESTClient('http://localhost:8080')

    def "Should return 200 and a list of all city names"() {
        when:
        def resp = endpoint.get([path: '/'])
        then:
        with(resp) {
            status == 200
            contentType == 'application/json'
        }
        resp.data.size == 4
        resp.data[0].name == 'London'
        resp.data[1].name == 'Paris'
        resp.data[2].name == 'New York'
        resp.data[3].name == 'San Francisco'
    }

    def "Should return 200 and London"() {
        when:
        def resp = endpoint.get([ path: '/near/Paris'])
        then:
        with(resp) {
            status == 200
            contentType == 'application/json'
        }
       with(resp.data) {
            name == 'London'
       }
    }

    def "Should return 200 and New York"() {
        when:
        def resp = endpoint.get([ path: '/near/San Francisco'])
        then:
        with(resp) {
            status == 200
            contentType == 'application/json'
        }
        with(resp.data) {
            name == 'New York'
        }
    }

    def "Should throw a HttpResponseException with status code 404"() {
        when:
        def resp = endpoint.get([ path: '/near/Sundsvall'])
        then:
        HttpResponseException ex = thrown()
        ex.statusCode == 404
    }
}