package se.sprezzatura.gsb.domain

import grails.mongodb.geo.Point;
import grails.persistence.Entity

import org.bson.types.ObjectId

/**
 * Created by Thomas on 30/09/2014.
 */

@Entity
class City {
    ObjectId id
    String name
    Point location

    static constraints = {
        name blank: false
        location nullable: false
    }

    static mapping = {
        location geoIndex: '2dsphere'
    }
}
