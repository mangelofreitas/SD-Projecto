function add(divName)
{
    var newdiv = document.createElement('div');
    newdiv.innerHTML ="<s:textfield key='typeProduct' label='TypeProduct'/>";
    document.getElementById(divName).appendChild(newdiv);
}