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

function getConversations(){
let data=[];
console.log("Getting All Conversations");
axios.get('/getConversations')
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
  console.log(data);
  for(var i=0;i<data.length;i++){
  document.getElementById("messageArea").innerHTML+="<div onclick=openMessage(\""+data[i].conversationid+"\") class=\"messageList\" id=\""+data[i].conversationid+"\"><p>"+data[i].username+"</p></div>";
  }
  }
}
  })
  .catch(function (error) {
    console.log(error);
  });
}

function openMessage(conversationid){
let urlParams =window.origin+"/chat.html?id="+conversationid;
console.log(urlParams);
window.location.href=urlParams;
}