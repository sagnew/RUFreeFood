var express = require('express');
var app = express();
var freefood = require('./freefood')();

//Set up ejs for rendering
app.engine('.html', require('ejs').__express);
app.set('views', __dirname + '/views');
app.set('view engine', 'html');
app.use(express.static(__dirname + '/static'));

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
