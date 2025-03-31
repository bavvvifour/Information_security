let money = 0;
let clickGain = 1;
let autoGain = 1;
let interval;

const clickerButton = document.getElementById("main-clicker");
const moneyText = document.getElementById("money");
const shopButtons = [document.getElementById("b1"), document.getElementById("b2"), document.getElementById("b3")];

function updateMoney(check = true) {
    moneyText.textContent = `${money} е-балл`;
    if (check) checkPrices();
}

function autoMoney() {
    if (interval) return;

    interval = setInterval(() => {
        money += autoGain;
        updateMoney();
        saveProgress();
    }, 1000);
}

function checkPrices() {
    shop.forEach(item => item.element.disabled = money < item.price);
}

class ShopElement {
    constructor(id, priceFunc, clickFunc) {
        this.id = id;
        this.element = document.getElementById(id);
        this.element.onclick = this.purchase.bind(this);
        this.text_element = this.element.getElementsByTagName("b")[0];

        this.updatePrice = priceFunc;
        this.onClick = clickFunc;

        this.price = 0;
        this.purchaseLvl = 1;
        this.update();
    }

    update() {
        this.updatePrice(this);
        this.text_element.innerHTML = `<b>${this.price} e-балл</b>`;
    }

    purchase() {
        if (money >= this.price) {
            money -= this.price;
            this.purchaseLvl++;
            this.onClick(this);
            this.update();
            updateMoney();
            saveProgress();

            if (this.id === "b2" || this.id === "b3") {
                clearInterval(interval);
                interval = null;
                autoMoney();
            }
        } else {
            alert("Недостаточно денег!");
        }
    }

}

const shop = [
    new ShopElement("b1", obj => obj.price = clickGain * 25 * obj.purchaseLvl, obj => clickGain *= 2),
    new ShopElement("b2", obj => obj.price = 200 * obj.purchaseLvl, obj => autoGain += 4),
    new ShopElement("b3", obj => obj.price = (autoGain / 2) * 30 * obj.purchaseLvl + 500, obj => autoGain *= 2)
];

async function loadProgress() {
    const savedData = await sendRequest("/loadScore", "GET");

    if (savedData) {
        money = savedData.score;
        clickGain = savedData.clickGain;
        autoGain = savedData.autoGain;

        shop[0].purchaseLvl = savedData.b1Level;
        shop[1].purchaseLvl = savedData.b2Level;
        shop[2].purchaseLvl = savedData.b3Level;

        shop.forEach(item => item.update());

        if (shop[2].purchaseLvl > 1) {
            clearInterval(interval);
            interval = null;
            autoMoney();
        }
    }
    updateMoney();
}



document.addEventListener('DOMContentLoaded', async () => {
    await loadProgress();
    if (shop[1].purchaseLvl > 1) {
        autoMoney();
    }
});


async function saveProgress() {
    const data = {
        score: money,
        clickGain: clickGain,
        autoGain: autoGain,
        clickMultiplier: 1,
        autoGainBonus: 0,
        autoMultiplier: 1,
        b1Level: shop[0].purchaseLvl,
        b2Level: shop[1].purchaseLvl,
        b3Level: shop[2].purchaseLvl
    };

    await sendRequest("/save", "POST", data);
}

clickerButton.addEventListener("click", () => {
    money += clickGain;
    updateMoney();
    saveProgress();
});

async function sendRequest(url, method, body = null) {
    const headers = {"Content-Type": "application/json"};

    const response = await fetch(url, {
        method,
        headers,
        body: body ? JSON.stringify(body) : null,
        credentials: "include"
    });

    const text = await response.text();
    try {
        return JSON.parse(text);
    } catch (e) {
        return text;
    }
}

window.addEventListener("load", loadProgress);

