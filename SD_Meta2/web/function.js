var webSocket;
var notifications = document.getElementById("notifications");

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
    console.log(split);
    for(i=2;i<split.length;i=i+11)
    {
        newDiv.innerHTML += "<form action='donate' method='post'><button type='submit' class='btn btn-primary' methdod='execute'>" +
            "Product Type:"+split[i+3]+"<br> Votes:"+split[i+7]+"" +
            "<input id='producttypechoose' name='Producttypechoose' type='text' value='"+split[i]+"' hidden>" +
            "<input id='projectID' name='ProjectID' type='text' value='"+projectID+"' hidden>" +
            "<input id='valuedonate' name='Valuedonate' type='text' value='"+rewardvalue+"' hidden>" +
            "</button><br><br></form>";
    }
    document.getElementById('donateptypes').appendChild(newDiv);
}

function openSocket()
{
    if('WebSocket' in window)
    {
        webSocket = new WebSocket('ws://'+window.location.host+'/notification');
    }
    else if('MozWebSocket' in window)
    {
        webSocket = new MozWebSocket('ws://'+window.location.host+'/notification');
    }
    else
    {
        writeConsole("Erro: WebSocket is not supported by this browser!");
        return;
    }


    webSocket.onopen = function(event)
    {
        if(event.data == undefined)
        {
            return;
        }
        writeConsole("Socket Opened!");
    };

    webSocket.onmessage = function(event)
    {
        showNotification(event.data);
    }

    webSocket.onclose = function(event)
    {
        writeConsole(event.data);
        writeConsole("Connection closed");
    }
}

function showNotification(text)
{
    var newLi = document.createElement('li');
    newLi.innerHTML = "<a href='#'>"+text+"</a>";
    notifications.appendChild(newLi);
}

function writeConsole(text)
{
    console.log(text);
}