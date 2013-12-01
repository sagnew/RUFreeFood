function FreeFood(){

    var http = require('http')
        , xml2js = require('xml2js');
    var eventsxml = '';
    var options = {
        host: 'ruevents.rutgers.edu',
        path: '/events/getEventsRss.xml'
    }

    var foodWords = ["food", "appetizer", "snack", "pizza", "lunch", "dinner", "breakfast", "meal",
        "candy", "drinks", "punch", " pie ", "pie.",  "cake", "soda", "chicken", "wings", "burger",
        "burrito", "stuff", "bagel", "coffee", " ice ", "cream", "donut", "beer",
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

    var getFreeFoodEvents = function(callback){
        var freefoodevents = [];
        http.get(options, function (response){
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
                                'when': events[i]['event:beginDateTime'],
                                'foodWord': foodWord
                            }
                            freefoodevents.push(freeFoodEvent);
                        }
                   }
                   callback(freefoodevents);
                });
            })
        }).on('error', function(e){console.log(e);});
    }

    return {
        getFreeFoodEvents: getFreeFoodEvents
    };
}

module.exports = FreeFood;
