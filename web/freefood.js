var http = require('http')
    , xml2js = require('xml2js');
var eventsxml = '';
var options = {
    host: 'ruevents.rutgers.edu',
    path: '/events/getEventsRss.xml'
}

function printEvents(events){
    var i;
    for(i = 0; i<events.length; i++){
        console.log(events[i]);
    }
}

var freeFoodEvents = function(){
    var freefoodevents = [];
    http.get(options, function (response){
        var completeResponse = '';
        response.on('data', function(chunk){
            completeResponse += chunk;
        }); response.on('end', function(){
            eventsxml = completeResponse;
            var parser = new xml2js.Parser();
            var events = '';
            parser.parseString(eventsxml, function(err, result){
               events = result;
               events = events['rss']['channel'][0]['item'];
               var i;
               for (i = 0; i < events.length; i++) {
                    if(events[i].description[0].toString().indexOf('food') !== -1){
                        console.log(events[i]);
                        freeFoodEvent = {
                            'title': events[i].title,
                            'description': events[i].description,
                            'location': events[i]['event:location'],
                            'when': events[i]['event:beginDateTime']
                        }
                        freefoodevents.push(freeFoodEvent);
                    }
               }
               printEvents(freefoodevents);
            });
        })
    }).on('error', function(e){console.log(e);});
}();
