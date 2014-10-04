package se.sprezzatura.gsb.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET

import com.mongodb.BasicDBObject

import grails.mongodb.geo.Point

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import se.sprezzatura.gsb.domain.City

import javax.annotation.PostConstruct

/**
 * Created by Thomas on 30/09/2014.
 */
@RestController
class CityController {

    @RequestMapping(value = "/", method = GET)
    List index() {
        City.list().collect { [name: it.name] }
    }

    @RequestMapping(value = "/near/{cityName}", method = GET)
    ResponseEntity near(@PathVariable String cityName) {
        def city = City.where { name == cityName }.find()
        if (city) {
            List<City> closest = City.findAllByLocationNear(city.location)
            return new ResponseEntity([name: closest[1].name], HttpStatus.OK)
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostConstruct
    void populateCities() {
        City.withTransaction {
            City.collection.remove(new BasicDBObject())
            City.saveAll(
                    [new City(name: "London",
                            location: Point.valueOf([-0.125487, 51.508515])),
                     new City(name: "Paris",
                             location: Point.valueOf([2.352222, 48.856614])),
                     new City(name: "New York",
                             location: Point.valueOf([-74.005973, 40.714353])),
                     new City(name: "San Francisco",
                             location: Point.valueOf([-122.419416, 37.774929]))]
            )
        }
    }
}