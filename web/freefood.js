function FreeFood(){

    //Required node libraries
    var http = require('http'),
        xml2js = require('xml2js'),
        moment = require('moment'),
        async = require('async');

    //"Global" variables
    var eventOptions = {
            host: 'ruevents.rutgers.edu',
            path: '/events/getEventsRss.xml'
        },
        studentlifeOptions = {
            host: "getinvolved.rutgers.edu",
            path: "/feed.php"
        },
        foodWords = [
            "appetizer", "snack", "pizza", "lunch", "dinner", "breakfast", "meal",
            "candy", "drinks", "punch", " pie ", " served", " serving", "pie.",  "cake", "soda", "chicken", "wings", "burger",
            "burrito", "bagel", "poporn", " ice ", "cream", "donut", "beer",
            "subs", "hoagie", "sandwich", "turkey", "supper", "brunch", "takeout", "refreshment",
            "beverage", "cookie", "brownie", "chips", "soup", "grill", "bbq", "barbecue"
        ];

    //Create a containsAny method for Strings
    String.prototype.containsAny = function(list){
        //Determines whether a string contains any of the words in the given list
        //Returns the word that the string contains, or null otherwise
        var i;
        for(i = 0; i < list.length; i++){
             if(this.toLowerCase().indexOf(list[i]) !== -1){
                return list[i];
             }
        }
        return null;
    };

    var formatDate = function(time){
        return moment(time.substring(0, time.length - 4));
    };

    var handleRequest = function(response, cb){
        var completeResponse = '';
        var freefoodevents = [];

        response.on('data', function(chunk){
            completeResponse += chunk;
        });

        var eventsxml = '';
        response.on('end', function(){
            eventsxml = completeResponse;
            var parser = new xml2js.Parser();
            var events = '';

            parser.parseString(eventsxml, function(err, result){
                events = result;
                events = events['rss']['channel'][0]['item'];

                for (var i = 0; i < events.length; i++) {
                     foodWord = events[i].description[0].toString().containsAny(foodWords)
                     if(foodWord !== null){
                         freeFoodEvent = {
                             'title': events[i].title,
                             'description': events[i].description,
                             'location': events[i]['event:location'],
                             'when': formatDate(events[i]['event:beginDateTime'][0]).format("h:mma ddd MMMM Do, YYYY"),
                             'moment': formatDate(events[i]['event:beginDateTime'][0]),
                             'link': events[i]['link'],
                             'foodWord': foodWord
                         }

                         if(typeof(freeFoodEvent['location']) === 'undefined'){
                             // Just in case
                             freeFoodEvent['location'] = "See description/link"
                         }

                         if(freeFoodEvent.moment.isAfter()){
                             //Only insert dates after the current date
                             freefoodevents.push(freeFoodEvent);
                         }
                     }
                }
                console.log("One response finished");
                console.log(freefoodevents);
                cb(null, freefoodevents);
            });
        });
    };

    var getFreeFoodEvents = function(callback){

        async.parallel([
            function (cb){
                console.log("First called");
                http.get(studentlifeOptions, function (response){
                    handleRequest(response, cb);
                }).on('error', function(e){console.log(e);});
            },
            function (cb){
                console.log("Second called");
                http.get(eventOptions, function (response){
                    handleRequest(response, cb);
                }).on('error', function(e){console.log(e);});
            }
        ],
        function (err, results){
            if(err){
                console.log(err);
            }
            results = results[0].concat(results[1]);

            results.sort(function(event1 ,event2){
                a = event1.moment;
                b = event2.moment;
                return b.isAfter(a)?-1:a.isAfter(b)?1:0;
            });
            callback(results);
        });
    };

    return {
        getFreeFoodEvents: getFreeFoodEvents
    };
}

module.exports = FreeFood;
