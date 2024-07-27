let host = "http://127.0.0.1:8100";
let varification_code = "3b4gBY4C75sx8DD9lvZL0mM1wLGnysYHFERx9136YnURDBbXwb";

// set store
function setStore(data) {
    console.log(data);
    $("#itemAccountId").text(data["username"]);
    $("#offersList").empty();
    $("#nightmarketList").empty();
    if (data["singleItemOffers"].length > 0) {
        data["singleItemOffers"].forEach((offerItem) => {
            $("#offersList").append(getDivOffer(offerItem["displayName"], offerItem["displayIcon"], offerItem["cost"]))
        });
    } else {
        console.log("singleItemOffers is empty");
    }
    if (data["nightMarket"].length > 0) {
        data["nightMarket"].forEach((offer) => {
            $("#nightmarketList").append(getDivNightmarket(offer["displayName"], offer["displayIcon"], offer["cost"], offer["finalCost"], offer["discountPercent"]))
        });
    } else {
        console.log("nightMarket is empty");
    }
    $("#itemCards").removeClass("invisible");
    $("#itemCards").addClass("visible");
    setVisible("home");
}
// login
function login() {
    if ($("#loginUsername").val().length < 1 || $("#loginPassword").val().length < 1) {
        return;
    }
    $("#directLogin-statusMessage").text("ログイン中...")
    $.ajax({
        url:`${host}/login`,
        type:"POST",
        contentType: "application/json",
        dataType: "json",
        crossDomain: true,
        data:JSON.stringify({
            "username":$("#loginUsername").val(),
            "password":$("#loginPassword").val(),
            "remember":$("#loginRemember").prop("checked")
        })
    }).done((data) => {
        setStore(data["data"]);
        $("#directLogin-statusMessage").text("ログイン成功");
        //$("#loginUsername").val("");
        //$("#loginPassword").val("");
    }).fail(() => {
        $("#directLogin-statusMessage").text("ログイン失敗");
        //$("#loginPassword").val("");
    })
}

// container
var currentContainer = "home";
var containers = [ "directLogin", "home", "accounts", "settings" ];

// setvisible container
function setVisible(targetContainer) {
    if (!containers.includes(targetContainer)) {
        return
    }
    containers.forEach((container) => {
        if (container == targetContainer) {
            $("#" + container + "Button").addClass("selected");
            $("#" + container).addClass("visible");
            $("#" + container).removeClass("invisible");
            return;
        }
        $("#" + container).addClass("invisible");
        $("#" + container).removeClass("visible");
        $("#" + container + "Button").removeClass("selected");
    });
    currentContainer = targetContainer;
}

// register containers
containers.forEach((container) => {
    $("#" + container + "Button").on("click", function() {
        setVisible(container);
    });
});

// set message to current container
function setStatusMessage(container, message) {
    if (!containers.includes(container)) {
        return;
    }
    $("#" + container + "-statusMessage").text(message);
}

// load accounts
function loadAccounts() {
    setStatusMessage("accounts", "ロード中...");
    $.ajax({
        url:`${host}/accounts`,
        type:"GET",
        crossDomain: true
    }).done((data) => {
        var saved = data;
        var count = 0;
        $("#accountList").empty();
        saved.forEach((user) => {
            count += 1;
            id = "loginSavedAccount-" + user;
            $("#accountList").append(getDivAccount(user, true, id));
            $(`#${id}`).on("click", () => {
                savedLogin(user);
            });
        });
        $("#accountsText").empty()
        if (count == 0) {
            $("#accountsText").append(`accounts: <span class="text-danger">${count}</span>`)
        } else {
            $("#accountsText").append(`accounts: <span class="text-info">${count}</span>`)
        }
        setStatusMessage("accounts", "ロード完了");
    })
    .fail(() => {
       setStatusMessage("accounts", "ロード失敗");
    });
}

// login saved account
function savedLogin(savedAccount) {
    if (savedAccount != null) {
        setStatusMessage("accounts", "ログイン中...");
        $.ajax({
            url:`${host}/accounts`,
            type:"POST",
            contentType: "application/json",
            dataType: "json",
            crossDomain: true,
            data:JSON.stringify({
                "username":savedAccount
            })
        }).done((data) => {
            setStatusMessage("accounts", "ログイン成功");
            setStore(data["data"]);
        }).fail(() => {
            setStatusMessage("accounts", "ログイン失敗");
        })
    }
}

// login txt account
function txtLogin(txtAccount) {
    if (txtAccount != null) {
        setStatusMessage("accounts", "ログイン中...");
        $.ajax({
            url:`${host}/txt/login`,
            type:"POST",
            contentType: "application/json",
            dataType: "json",
            crossDomain: true,
            data:JSON.stringify({
                "username":txtAccount
            })
        }).done((data) => {
            setStatusMessage("accounts", "ログイン成功");
            setStore(data);
        }).fail(() => {
            setStatusMessage("accounts", "ログイン失敗");
        })
    }
}

function loadSettings() {
    $.ajax({
        url:`${host}/settings`,
        type:"GET",
        crossDomain:true
    }).done((data) => {
        $("#webhookURLText").empty();
        $("#webhookURLText").append(`URL: <span class="text-info">${data["url"]}</span>`)
        $("#webhookIconText").empty();
        $("#webhookIconText").append(`Icon: <span class="text-info">${data["icon"]}</span>`)
        $("#webhookIconSrc").attr("src", data["icon"]);
    })
}

// send webhook
function executeWebhook() {
    setStatusMessage("webhook", "送信中...");
    $.ajax({
        url:`${host}/webhook`,
        type:"POST",
        headers:{
            "varification_code":varification_code
        },
        crossDomain:true
    }).done((data)=>{
        setStatusMessage("webhook", "送信完了");
    }).fail(() => {
        setStatusMessage("webhook", "送信失敗");
    })
}

// set button events
function setEvents() {
    $("#submitLogin").on("click", login);
    $("#executeWebhook-Button").on("click", executeWebhook);
    $("#reloadAccounts-Button").on("click", loadAccounts);
    $("#loginTxtAccountButton").on("click", txtLogin);
    $("#loginSavedAccountButton").on("click", savedLogin);
}

function getDivAccount(name, isSaved, id) {
    icon = isSaved ? `<i class="bi bi-person-fill-down"></i>` : `<i class="bi bi-person-fill-add"></i>`
    div = 
    `<a href="#" class="d-inline m-2 border rounded border-white text-decoration-none" id="${id}">
        <p class="fs-4 p-2 text-info m-0">
            <span class="text-white">
                ${icon}
            </span>
            ${name}
        </p>
    </a>`
    return div;
}

function getDivOffer(name, img, cost) {
    div = 
    `<div class="my-3 d-inline" style="height: 250px;width:400px">
        <div class="position-relative" style="height:100%;">
            <h5 class="position-absolute top-0 start-0 fs-3 text-white">${name}</h5>
            <img class="position-absolute top-50 start-0 translate-middle-y" src="${img}" style="width: 300px;">
            <p class="position-absolute bottom-0 start-0 text-info fs-5 my-0">${cost} <span class="text-white">VP</span></p>
        </div>
    </div>`
    return div;
}

function getDivNightmarket(name, img, originalCost, finalCost, discountPercent) {
    div = 
    `<div class="my-3 d-inline" style="height: 250px;width:400px">
        <div class="position-relative" style="height:100%;">
            <h5 class="position-absolute top-0 start-0 fs-3 text-white">${name}</h5>
            <img class="position-absolute top-50 start-0 translate-middle-y" src="${img}" style="width: 300px;">
            <p class="position-absolute bottom-0 start-0 text-danger fs-5 my-0"><span class="fs-6 text-decoration-line-through">${originalCost}</span> -${discountPercent}%<br>
            <span class="text-info fs-5 my-0">${finalCost} <span class="text-white">VP</span></span>
            </p>
        </div>
    </div>`
    return div;
}

/*
$("#nightmarketList").append(getDivNightmarket("Trash", "https://media.valorant-api.com/weaponskinlevels/11c1c7fb-4fe7-1f6f-e4c4-47a9f46f7ea2/displayicon.png", 3550, 2550, 25))
$("#nightmarketList").append(getDivNightmarket("Trash", "https://media.valorant-api.com/weaponskinlevels/11c1c7fb-4fe7-1f6f-e4c4-47a9f46f7ea2/displayicon.png", 3550, 2550, 25))
$("#nightmarketList").append(getDivNightmarket("Trash", "https://media.valorant-api.com/weaponskinlevels/11c1c7fb-4fe7-1f6f-e4c4-47a9f46f7ea2/displayicon.png", 3550, 2550, 25))
$("#nightmarketList").append(getDivNightmarket("Trash", "https://media.valorant-api.com/weaponskinlevels/11c1c7fb-4fe7-1f6f-e4c4-47a9f46f7ea2/displayicon.png", 3550, 2550, 25))
$("#nightmarketList").append(getDivNightmarket("Trash", "https://media.valorant-api.com/weaponskinlevels/11c1c7fb-4fe7-1f6f-e4c4-47a9f46f7ea2/displayicon.png", 3550, 2550, 25))
$("#nightmarketList").append(getDivNightmarket("Trash", "https://media.valorant-api.com/weaponskinlevels/11c1c7fb-4fe7-1f6f-e4c4-47a9f46f7ea2/displayicon.png", 3550, 2550, 25))
*/

setEvents();
loadAccounts();
loadWebhookInfo();