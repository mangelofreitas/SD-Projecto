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