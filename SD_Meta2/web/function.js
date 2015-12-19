var webSocketNotification;
var webSocketMessage;
var webSocketRenewMoney;
var notifications = document.getElementById("notifications");
var dropdownnotifications = document.getElementById("dropdownnotifications");
var messages = document.getElementById("messages");;
var dropdownmessages = document.getElementById("dropdownmessages");






function add(divName,idDiv,idInput,nameInput)
{
    var newdiv = document.createElement('div');
    newdiv.id = idDiv;
    if(divName == 'ptype')
    {
        newdiv.innerHTML ="<input id="+idInput+" type='text' class='form-control' placeholder='Product Type' name="+nameInput+" required>";
    }
    if(divName == 'rwrd')
    {
        newdiv.innerHTML ="<input id="+idInput+" type='text' class='form-control' placeholder='Product Type' name="+nameInput+" required>";
        newdiv.innerHTML += "<input id='valueReward' type='number' class='form-control' placeholder='100' name='ValueReward' required>";
    }
    document.getElementById(divName).appendChild(newdiv);
}
function remove_div(divName,idDiv)
{
    var div = document.getElementById(divName);
    var divToRemove = document.getElementById(divName).lastElementChild;
    var divRemove = document.getElementById(idDiv);
    if(divRemove!=null)
    {
        div.removeChild(divToRemove);
    }
}

function add_reward(divName,buttonID,projectID)
{
    var newdiv = document.createElement('div');
    newdiv.innerHTML = "<form action='editreward' method='post'>" +
        "<input id='projectid' type='number' name='Projectid' value="+projectID+" hidden>" +
        "<input id='type' type='text' name='Type' value='add' hidden>" +
        "<input id='reward' type='text' class='form-control' placeholder='Reward' name='Reward' required>" +
        "<input id='valueReward' class='form-control' placeholder='100' type='number' name='ValueReward' required>" +
        "<input style='margin-top: 50px' class='btn btn-primary btn-xl' value='Add' type='submit'></form>";
    document.getElementById(divName).appendChild(newdiv);
    var div = document.getElementById(divName);
    var button = document.getElementById(buttonID);
    div.removeChild(button);
}

function donateMoney(rewardName,rewardvalue,producttypes,projectID)
{
    while(document.getElementById('types')!=null)
    {
        document.getElementById('donateptypes').removeChild(document.getElementById('types'));
    }
    document.getElementById('descriptiondonate').innerHTML ='Description: '+rewardName;
    document.getElementById('valuedonate').innerHTML = 'Value of Reward: '+rewardvalue;
    var newDiv = document.createElement('div');
    newDiv.id = 'types';
    var discard = producttypes.split("]");
    var split = discard[0].split(" ");
    for(var i=2;i<split.length;i=i+11)
    {
        newDiv.innerHTML += "<form action='donate' method='post'><button type='submit' class='btn btn-primary' methdod='execute' onclick=sendDonate('"+projectID+"',"+rewardvalue+")>" +
            "Product Type:"+split[i+3]+"<br> Votes:"+split[i+7]+"" +
            "<input id='producttypechoose' name='Producttypechoose' type='text' value='"+split[i]+"' hidden>" +
            "<input id='projectID' name='ProjectID' type='text' value='"+projectID+"' hidden>" +
            "<input id='valuedonate' name='Valuedonate' type='text' value='"+rewardvalue+"' hidden>" +
            "</button><br><br></form>";
    }
    document.getElementById('donateptypes').appendChild(newDiv);
}

function openSocketRenewMoney()
{
    if('WebSocket' in window)
    {
        webSocketRenewMoney = new WebSocket('ws://'+window.location.host+'/renewMyMoney');
    }
    else if('MozWebSocket' in window)
    {
        webSocketRenewMoney = new MozWebSocket('ws://'+window.location.host+'/renewMyMoney');
    }
    else
    {
        writeConsole("Error: WebSocket is not supported by this browser!");
        return;
    }

    webSocketRenewMoney.onopen = function(event)
    {
        if(event.data == undefined)
        {
            return;
        }
        writeConsole("Socket Renew Money Opened!");
    };

    webSocketRenewMoney.onmessage = function(event)
    {
        $('#userMoney').text('Available Money: ' + event.data);
    }

    webSocketRenewMoney.onclose = function(event)
    {
        writeConsole(event.data);
        writeConsole("Connection closed");
    }
}

function openSocketMessage()
{
    if('WebSocket' in window)
    {
        webSocketMessage = new WebSocket('ws://'+window.location.host+'/message');
    }
    else if('MozWebSocket' in window)
    {
        webSocketMessage = new MozWebSocket('ws://'+window.location.host+'/message');
    }
    else
    {
        writeConsole("Error: WebSocket is not supported by this browser!");
        return;
    }

    webSocketMessage.onopen = function(event)
    {
        if(event.data == undefined)
        {
            return;
        }
        writeConsole("Socket Message Opened!");
    };

    webSocketMessage.onmessage = function(event)
    {
        var split = event.data.split(":");
        showMessages(split[1], split[0]);
    }
    webSocketMessage.onclose = function(event)
    {
        writeConsole(event.data);
        writeConsole("Connection closed");
    }
}

function openSocketNotification()
{
    if('WebSocket' in window)
    {
        webSocketNotification = new WebSocket('ws://'+window.location.host+'/notification');
    }
    else if('MozWebSocket' in window)
    {
        webSocketNotification = new MozWebSocket('ws://'+window.location.host+'/notification');
    }
    else
    {
        writeConsole("Error: WebSocket is not supported by this browser!");
        return;
    }


    webSocketNotification.onopen = function(event)
    {
        if(event.data == undefined)
        {
            return;
        }
        writeConsole("Socket Notification Opened!");
    };

    webSocketNotification.onmessage = function(event)
    {
        showNotification(event.data);
    }

    webSocketNotification.onclose = function(event)
    {
        writeConsole(event.data);
        writeConsole("Connection closed");
    }
}

function showNotification(text)
{
    var url = document.URL;
    if(url.indexOf("projects") > -1 || url.indexOf("donate") > -1 || url.indexOf("sendmessage") > -1 || url.indexOf("editreward") > -1 || url.indexOf("sendreply") > -1)
    {
        var split = text.split(",");
        text = split[0];
        $('#userMoney').text('Available Money: ' + event.data);
        $('#currentAmount_'+split[1]).text('Current Amount: '+split[2]);
    }
    if(text.indexOf("you") > -1)
    {
        var newLi = document.createElement("LI");
        newLi.innerHTML = "<a>"+text+"</a>";
        notifications.appendChild(newLi);
        dropdownnotifications.className = "btn-warning";
    }
}

function showMessages(text,id)
{
    var newLi = document.createElement("LI");
    var split = id.split("_");
    if(split[0] == "message")
    {
        newLi.innerHTML = "<a class='page-scroll' href='projects?type=myprojects#"+id+"'>"+text+"</a>";
    }
    else if(split[0] == "reply")
    {
        newLi.innerHTML = "<a class='page-scroll' href='projects?type=actualprojects#"+id+"'>"+text+"</a>";
    }
    messages.appendChild(newLi);
    dropdownmessages.className = "btn-warning";
}

function writeConsole(text)
{
    console.log(text);
}

function sendRequestRenewMoney()
{
    var text = "My Money?";
    webSocketRenewMoney.send(text);
}

function sendDonate(projectID,value)
{
    var text = projectID+" "+value;
    webSocketNotification.send(text);
}

function sendMessage(type,differedID)
{
    var text = type+" "+differedID;
    webSocketMessage.send(text);
}

function saw(text)
{
    if(text == 'messages')
    {
        dropdownmessages.className = "";
    }
    else if(text == 'notifications')
    {
        dropdownnotifications.className = "";
    }
}

window.onload = openSocketRenewMoney();
window.onload = openSocketNotification();
window.onload = openSocketMessage();

setInterval(function(){sendRequestRenewMoney()},5000);

setTimeout(function(){sendRequestRenewMoney();},5000);