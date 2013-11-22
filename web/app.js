var express = require('express');
var app = express();
var freefood = require('./freefood')();

function printEvents(events){
    var i;
    for(i = 0; i < events.length; i++){
        console.log(events[i]);
    }
}

freefood.getFreeFoodEvents(printEvents);

app.get('/', function(req, res){
    res.send('Free food!');
});

app.listen(3000);
console.log('Listening on port 3000');
