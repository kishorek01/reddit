function logout(){
axios.get('/logout')
  .then(function (response) {
    if(response.data.code==500){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);

      }else{
  data=response.data;
  if(data.code==200){
  console.log(response.data);
    toastr["success"]("Logout Successful","Success");
  setTimeout(function(){
  document.location="login.html";
  },500)
  }
}
  })
  .catch(function (error) {
    console.log(error);
  });
}


function sendMessage(){
var message=document.getElementById("MessagesInput").value;
let querySearch=window.location.search;
querySearch=querySearch.replace("?id=","");
let conversationId=querySearch;
const params = new URLSearchParams();
params.append('conversationid', conversationId);
params.append('message', message);
axios.post('/postMessage',params)
  .then(function (response) {
  console.log(response);
    if(response.data){
    if(response.data.code==201){
toastr["error"](response.data.data.message, "Error");
document.location="login.html";
}else if(response.data.code==200){
document.getElementById("MessagesInput").value="";
loadMessages(response.data.data.data);
}
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}

function deleteMessage(messageId){
var message=document.getElementById("MessagesInput").value;
let querySearch=window.location.search;
querySearch=querySearch.replace("?id=","");
let conversationId=querySearch;
const params = new URLSearchParams();
params.append('conversationid', conversationId);
params.append('messageid',messageId)
axios.post('/deleteMessage',params)
  .then(function (response) {
  console.log(response);
    if(response.data){
    if(response.data.code==201){
toastr["error"](response.data.data.message, "Error");
document.location="login.html";
}else if(response.data.code==200){
document.getElementById("MessagesInput").value="";
loadMessages(response.data.data.data);
}
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}

function loadMessages(data){
document.getElementById("messageArea").innerHTML="";
  console.log(data);
  var keys=Object.keys(data);
  var owner=getCookie("user");
  for(var i=0;i<keys.length;i++){
  var message=data[keys[i]].message;
  if(message=="" || message==null){
  message="This Message Was Deleted";
  }
//  console.log(document.cookie);

if(data[keys[i]].username==owner && message!="This Message Was Deleted"){
console.log(keys[i])
document.getElementById("messageArea").innerHTML+="<div class=\"messageReal\" id=\""+keys[i]+"\"><p><i><b>"+data[keys[i]].username+"</b> : "+message +"</i>     <svg onclick=\"deleteMessage(\'"+keys[i]+"\')\" style=\"    padding-left: 10px;color: red;cursor:pointer;\" xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-trash\" viewBox=\"0 0 16 16\"><path d=\"M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z\"/><path fill-rule=\"evenodd\" d=\"M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z\"/></svg></p></div>";
}else{

  document.getElementById("messageArea").innerHTML+="<div class=\"messageReal\" id=\""+keys[i]+"\"><p><i><b>"+data[keys[i]].username+"</b> : "+message +"</i></p></div>";
  }
  }
     document.getElementById("messageArea").scrollTop = document.getElementById("messageArea").scrollHeight;

  }
function getMessages(){
let querySearch=window.location.search;
querySearch=querySearch.replace("?id=","");
let conversationId=querySearch;
let data=[];
console.log("Getting All Conversations");
axios.get('/getMessages',{
params:{
conversationid:conversationId
}
})
  .then(function (response) {
    if(response.data.code==500){
    toastr["error"](response.data.data.message, "Error");
 setTimeout(function(){
  document.location="login.html";
  },3000);

      }else{
  data=response.data.data;
  if(data.data.length==0){
    document.getElementById("messageArea").innerHTML+="<div style=\"cursor: default;cursor: default;text-align: center;align-items: center;display: grid;grid-template-rows: auto;\" class=\"posts\"><p style=\"font-size: 20px;font-weight: 600;opacity: 0.5;font-style: italic;letter-spacing: 2px;\">No Messages Found</p></div>";
  }else{
  data=data.data;
loadMessages(data);
  }
}
  })
  .catch(function (error) {
    console.log(error);
  });
}

function getCookie(cname) {
  let name = cname + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let ca = decodedCookie.split(';');
  for(let i = 0; i <ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}