let stompClient = null;

const setConnected = (connected) => {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#users-actions").show();
  } else {
    console.log("name value: " + $("#name").val());
    clean();
    $("#users-actions").hide();
  }
};

const connect = () => {
  stompClient = Stomp.over(new SockJS("/users-service-websocket"));
  stompClient.connect({}, (frame) => {
    setConnected(true);
    console.log("Connected: " + frame);
    sendMsg();
    stompClient.subscribe("/topic/users", (message) =>
      showUsers(JSON.parse(message.body))
    );
  });
};

const disconnect = () => {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
};

const clean = () => {
  $("#name").val("");
  $("#password").val("");
};

const sendMsg = () => {
  stompClient.send(
    "/app/newUser",
    {},
    JSON.stringify({ name: $("#name").val(), password: $("#password").val() })
  );
  clean();
};

const showUsers = (usersJson) => {
  $("#usersTable").bootstrapTable("destroy");
  $("#usersTable").bootstrapTable({
    data: usersJson,
  });
};

$(function () {
  $("form").on("submit", (event) => {
    event.preventDefault();
  });
  $("#connect").click(connect);
  $("#disconnect").click(disconnect);
  $("#send").click(sendMsg);
  connect();
});
