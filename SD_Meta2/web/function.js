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