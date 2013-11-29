var express = require('express');
var app = express();
var freefood = require('./freefood')();

function printEvents(events){
    var i;
    for(i = 0; i < events.length; i++){
        console.log(events[i]['foodWord'] + "\n");
        console.log(events[i]['description'][0] + "\n");
    }
}

//Set up ejs for rendering
app.engine('.html', require('ejs').__express);
app.set('views', __dirname + '/views');
app.set('view engine', 'html');

freefood.getFreeFoodEvents(printEvents);

app.get('/', function(req, res){
    freefood.getFreeFoodEvents(function(events){
        res.render('index', {
            events: events,
            title: "Free Food"
        })
    });
});

app.listen(3000);
console.log('Listening on port 3000');
