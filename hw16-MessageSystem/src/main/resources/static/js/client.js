let client = null;

function connectWS() {
    client = Stomp.over(new SockJS("/users"));
    client.heartbeat.outgoing = 5000;
    client.heartbeat.incoming = 0;
    client.connect({}, onConnect, onErrorConnect);
}

function onConnect(info) {
    var userId = info.headers['user-name'];
    console.log('SockJS connection established. ID: ' + userId)

    client.subscribe(`/user/${userId}/topic/userlist`, onUserList);
    client.subscribe(`/user/${userId}/topic/requesteduser`, onUserRequestComplete);
    client.subscribe(`/topic/newuser`, onNewUser);

    client.send('/app/get');
}

function onUserList(message) {
    var array = JSON.parse(message.body);
    array.forEach(addUserToList)
}

function onNewUser(message) {
    var user = JSON.parse(message.body);
    addUserToList(user);
}

function onUserRequestComplete(message) {
    var user = JSON.parse(message.body);
    var result = $('#findResult');
    result.show();
    result.children()[0].textContent = `Имя: ${user['name']}`;
    result.children()[1].textContent = `Возраст: ${user['age']}`;
    result.children()[2].textContent = `Адрес: ${user['address']}`;
    result.children()[3].textContent = `Телефон: ${user['phones'].join(', ')}`;
}

function addUserToList(user) {
    var userList = $('#userList');
    userList.append(`<tr><th scope="row">${user.id}</th>
                             <td>${user.name}</td>
                             <td>${user.age}</td>
                             <td>${user.address}</td>
                             <td>${user.phones.join(', ')}</td>
                         </tr>`);
}

function onErrorConnect(error) {
    console.log('Cant connect to server: ' + error);
}

$(document).ready(function () {
    connectWS();
})