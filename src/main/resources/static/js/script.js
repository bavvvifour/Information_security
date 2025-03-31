// Глобальные переменные
let money = 0;
let clickGain = 1;
let autoGain = 1;
let token = null;
let interval;

const clickerButton = document.getElementById("main-clicker");
const moneyText = document.getElementById("money");
const shopButtons = [document.getElementById("b1"), document.getElementById("b2"), document.getElementById("b3")];

// Обновление денег на экране
function updateMoney(check = true) {
    moneyText.textContent = `${money} е-балл`;
    if (check) checkPrices();
}

// Автоматическое начисление денег
function autoMoney() {
    if (interval) clearInterval(interval); // предотвращаем дублирование интервалов
    interval = setInterval(() => {
        money += autoGain;
        updateMoney();
        saveProgress();
    }, 1000);
}

function onClick2(obj) {
    if (obj.purchaseLvl === 1) {  // Только при первой покупке
        autoMoney(1);
    }
}


// Проверка цен и обновление состояния кнопок магазина
function checkPrices() {
    shop.forEach(item => item.element.disabled = money < item.price);
}

// Класс магазина
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
        } else {
            alert("Недостаточно денег!");
        }
    }
}

// Настройки магазина
const shop = [
    new ShopElement("b1", obj => obj.price = clickGain * 25 * obj.purchaseLvl, obj => clickGain *= 2),
    new ShopElement("b2", obj => obj.price = 200 * obj.purchaseLvl, obj => autoGain += 5),
    new ShopElement("b3", obj => obj.price = autoGain * 30 * obj.purchaseLvl + 500, obj => autoGain *= 2)
];

// Восстановление магазина после загрузки
function restoreShop(data) {
    shop.forEach(item => {
        if (data.shop[item.id]) {
            item.purchaseLvl = data.shop[item.id];
            for (let i = 1; i < item.purchaseLvl; i++) {
                item.onClick(item);
            }
            item.update();
        }
    });
}

// Загрузка прогресса
function loadProgress() {
    const savedData = JSON.parse(localStorage.getItem("gameProgress"));
    if (savedData) {
        money = savedData.money;
        clickGain = savedData.clickGain;
        autoGain = savedData.autoGain;
        shop.forEach((item, index) => {
            item.purchaseLvl = savedData.shop[index] || 1;
            item.update();
        });

        // Запускаем автоначисление, если был куплен авто-клик
        if (shop[1].purchaseLvl > 1) {
            autoMoney(1);
        }
    }
    updateMoney();
}


document.addEventListener('DOMContentLoaded', async () => {
    await loadProgress();
    if (shop[1].purchaseLvl > 1) { // Проверяем, был ли куплен автокликер
        autoMoney();
    }
});


// Функция для сохранения прогресса
function saveProgress() {
    const data = {
        money,
        clickGain,
        autoGain,
        shop: shop.map(item => item.purchaseLvl) // Сохранение уровней покупок
    };
    localStorage.setItem("gameProgress", JSON.stringify(data));
}


// Обработчик кликов по кнопке кликера
clickerButton.addEventListener("click", () => {
    money += clickGain;
    updateMoney();
    saveProgress();
});

// Функция запроса к серверу
async function sendRequest(url, method, body = null) {
    const headers = { "Content-Type": "application/json" };

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

