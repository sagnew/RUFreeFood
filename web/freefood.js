function FreeFood(){

    var http = require('http')
        , xml2js = require('xml2js')
        , moment = require('moment');
    var freefoodevents = [];
    var eventsxml = '';
    var firstRequest = true, firstFinished = true;
    var eventOptions = {
        host: 'ruevents.rutgers.edu',
        path: '/events/getEventsRss.xml'
    }
    var studentlifeOptions = {
        host: "getinvolved.rutgers.edu",
        path: "/feed.php"
    }

    var foodWords = ["appetizer", "snack", "pizza", "lunch", "dinner", "breakfast", "meal",
        "candy", "drinks", "punch", " pie ", " served", " serving", "pie.",  "cake", "soda", "chicken", "wings", "burger",
        "burrito", "bagel", " ice ", "cream", "donut", "beer",
        "subs", "hoagie", "sandwich", "turkey", "supper", "brunch", "takeout", "refreshment",
        "beverage", "cookie", "brownie", "chips", "soup", "grill", "bbq", "barbecue"]

    var containsAny = function(str, list){
        //Determines whether a string contains any of the words in the given list
        //Returns the word that the string contains, or null otherwise
        var i;
        for(i = 0; i < list.length; i++){
             if(str.indexOf(list[i]) !== -1){
                return list[i];
             }
        }
        return null;
    }

    var formatDate = function(time){
        return moment(time.substring(0, time.length - 4)).format("h:mma ddd MMMM Do, YYYY");
    }

    var handleRequest = function(response, callback){
        var completeResponse = '';

        response.on('data', function(chunk){
            completeResponse += chunk;
        });

        response.on('end', function(){
            eventsxml = completeResponse;
            var parser = new xml2js.Parser();
            var events = '';

            parser.parseString(eventsxml, function(err, result){
                events = result;
                events = events['rss']['channel'][0]['item'];

                var i;
                for (i = 0; i < events.length; i++) {
                     foodWord = containsAny(events[i].description[0].toString(), foodWords)
                     if(foodWord !== null){
                         freeFoodEvent = {
                             'title': events[i].title,
                             'description': events[i].description,
                             'location': events[i]['event:location'],
                             'when': formatDate(events[i]['event:beginDateTime'][0]),
                             'link': events[i]['link'],
                             'foodWord': foodWord
                         }
                         if(typeof(freeFoodEvent['location']) === 'undefined'){
                             // Just in case
                             freeFoodEvent['location'] = "See description/link"
                         }
                         freefoodevents.push(freeFoodEvent);
                     }
                }

                if(!firstFinished){
                    firstRequest = true;
                    firstFinished = true;
                    freefoodevents.sort(function(event1 ,event2){
                        a = event1.when;
                        b = event2.when;
                        return b<a?-1:b>a?1:0;
                    });
                    callback(freefoodevents);
                }else{
                    firstFinished = false;
                }
            });
        });

        if(firstRequest){
            firstRequest = false;
            http.get(eventOptions, function (response){
                handleRequest(response, callback);
            }).on('error', function(e){console.log(e);});
        }
    }

    var getFreeFoodEvents = function(callback){
        freefoodevents = [];
        http.get(studentlifeOptions, function (response){
            handleRequest(response, callback);
        }).on('error', function(e){console.log(e);});
    }

    return {
        getFreeFoodEvents: getFreeFoodEvents
    };
}

module.exports = FreeFood;
