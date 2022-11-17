function checkSession(){
axios.get('/checkSession')
  .then(function (response) {
  console.log(response.data);
  if(response.data.session){
  document.location="home.html";
  }else{
  document.location="login.html";
  }
  })
  .catch(function (error) {
    console.log(error);
  });
}
