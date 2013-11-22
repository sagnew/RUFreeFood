function FreeFood(){

    var http = require('http')
        , xml2js = require('xml2js');
    var eventsxml = '';
    var options = {
        host: 'ruevents.rutgers.edu',
        path: '/events/getEventsRss.xml'
    }

    var foodWords = ['food', 'pizza']

    var containsAny = function(str, list){
        //Determines whether a string contains any of the words in the given list
        var i;
        for(i = 0; i < list.length; i++){
             if(str.indexOf(list[i]) !== -1){
                return true;
             }
        }
        return false;
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
                        if(containsAny(events[i].description[0].toString(), foodWords)){
                            freeFoodEvent = {
                                'title': events[i].title,
                                'description': events[i].description,
                                'location': events[i]['event:location'],
                                'when': events[i]['event:beginDateTime']
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
