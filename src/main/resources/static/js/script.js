// Глобальные переменные
let money = 0; // Количество денег
let clickGain = 1; // Количество денег за клик
let autoGain = 1; // Количество денег за автоматическое начисление
let shopData = []; // Данные магазина
let token = null;
let interval; // Интервал для автоматического начисления денег


const clickerButton = document.getElementById("main-clicker"); // Кнопка кликера
const moneyText = document.getElementById("money"); // Текст с количеством денег
const shopButtons = [document.getElementById("b1"), document.getElementById("b2"), document.getElementById("b3")]; // Кнопки магазина
const authModal = document.getElementById("auth-modal"); // Модальное окно авторизации
const closeModal = document.querySelector(".close"); // Кнопка закрытия модального окна
const loginBtn = document.getElementById("login-btn"); // Кнопка входа
const registerBtn = document.getElementById("register-btn"); // Кнопка регистрации
const authForm = document.getElementById("auth-form"); // Форма авторизации

// Основные функции

// Обновление отображения денег
function updateMoney(check = true) {
    moneyText.textContent = `${money} е-балл`; // Обновление текста с деньгами
    if (check) checkPrices(); // Проверка цен, если требуется
}

// Автоматическое начисление денег
function autoMoney(amount) {
    clearInterval(interval); // Очистка предыдущего интервала
    interval = setInterval(() => {
        money += autoGain; // Увеличение денег
        updateMoney(); // Обновление отображения
        saveProgress(); // Сохранение прогресса
    }, 1000 / amount); // Установка интервала
}

// Проверка цен в магазине
function checkPrices() {
    for (let i = 0; i < shop.length; i++) {
        shop[i].element.disabled = money < shop[i].price; // Отключение кнопок, если денег недостаточно
    }
}

// Обработка покупки
function onBuy(obj) {
    money -= obj.price; // Уменьшение денег
    updateMoney(false); // Обновление отображения без проверки цен
    for (let i = 0; i < shop.length; i++) {
        shop[i].element.disabled = true; // Временное отключение всех кнопок
    }
}

// Класс для элементов магазина
class ShopElement {
    constructor(id, newprice_func, onclick_func) {
        this.id = id; // ID элемента
        this.element = document.getElementById(id); // Элемент DOM
        this.element.onclick = this.purchase.bind(this); // Обработчик клика
        this.text_element = this.element.getElementsByTagName("b")[0]; // Текстовый элемент

        this._updatePrice = newprice_func; // Функция обновления цены
        this._onClick = onclick_func; // Функция обработки клика

        this.price = 0; // Цена
        this.purchaseLvl = 1; // Уровень покупки
        this.updatePrice(); // Обновление цены
    }

    // Обработка клика
    onClick() {
        this._onClick(this);
    }

    // Обновление цены
    updatePrice() {
        this._updatePrice(this);
    }

    // Обновление текста
    updateText() {
        this.text_element.innerHTML = `<b>${this.price} e-балл: </b>`;
    }

    // Обновление элемента
    update() {
        this.updatePrice();
        this.updateText();
    }

    // Покупка
    purchase() {
        if (money >= this.price) {
            money -= this.price; // Уменьшение денег
            this.purchaseLvl += 1; // Увеличение уровня покупки
            this.onClick(); // Обработка клика
            this.update(); // Обновление элемента
            updateMoney(); // Обновление отображения денег
            saveProgress(); // Сохранение прогресса
        } else {
            alert("Недостаточно денег!"); // Сообщение об ошибке
        }
    }
}

// Функции для кнопок магазина

// Обновление цены для первой кнопки
function newPrice1(obj) {
    obj.price = clickGain * 25 * obj.purchaseLvl;
}

// Обновление цены для второй кнопки
function newPrice2(obj) {
    obj.price = 200 * obj.purchaseLvl;
}

// Обновление цены для третьей кнопки
function newPrice3(obj) {
    obj.price = autoGain * 30 * obj.purchaseLvl + 500;
}

// Обработка клика для первой кнопки
function onClick1(obj) {
    clickGain *= 2; // Увеличение дохода за клик
}

// Обработка клика для второй кнопки
function onClick2(obj) {
    autoMoney(obj.purchaseLvl); // Запуск автоматического начисления
}

// Обработка клика для третьей кнопки
function onClick3(obj) {
    autoGain *= 2; // Увеличение автоматического дохода
}

// Магазин
const shop = [
    new ShopElement("b1", newPrice1, onClick1),
    new ShopElement("b2", newPrice2, onClick2),
    new ShopElement("b3", newPrice3, onClick3),
];

// Инициализация
updateMoney(); // Обновление отображения денег
for (let i = 0; i < shop.length; i++) {
    shop[i].update(); // Обновление всех элементов магазина
}

// Обработчик клика по кнопке кликера
clickerButton.addEventListener("click", () => {
    money += clickGain; // Увеличение денег
    updateMoney(); // Обновление отображения
    saveProgress(); // Сохранение прогресса
});

// Функция для отправки запроса на сервер
async function sendRequest(url, method, body = null) {
    const headers = {
        "Content-Type": "application/json",
    };
    if (token) {
        headers["Authorization"] = `Bearer ${token}`; // Добавление токена в заголовки
    }

    const response = await fetch(url, {
        method,
        headers,
        body: body ? JSON.stringify(body) : null,
    });

    if (!response.ok) {
        throw new Error("Ошибка запроса"); // Обработка ошибки
    }

    return response.json();
}

// Обработчик клика по кнопке
clickerButton.addEventListener("click", () => {
    money += clickGain;
    updateMoney();
    saveProgress();
});

// Открытие модального окна при нажатии на кнопку "Вход/Регистрация"
document.getElementById("b4").addEventListener("click", () => {
    authModal.style.display = "block";
});

// Закрытие модального окна при нажатии на крестик
closeModal.addEventListener("click", () => {
    authModal.style.display = "none";
});

// Закрытие модального окна при клике вне его
window.addEventListener("click", (event) => {
    if (event.target === authModal) {
        authModal.style.display = "none";
    }
});

// Обработка входа
loginBtn.addEventListener("click", async (event) => {
    event.preventDefault();
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    await login(username, password);
    authModal.style.display = "none";
});

// Обработка регистрации
registerBtn.addEventListener("click", async (event) => {
    event.preventDefault();
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    await register(username, password);
    authModal.style.display = "none";
});

// Инициализация при загрузке страницы
(async () => {
    // Пример регистрации и входа
    await register("user", "password");
    await login("user", "password");

    // Загрузка прогресса
    await loadProgress(1); // Замените на реальный ID пользователя
})();

// После успешного входа или регистрации
localStorage.setItem("username", username);

// При загрузке страницы
window.addEventListener("load", () => {
    const savedUsername = localStorage.getItem("username");
    if (savedUsername) {
        document.getElementById("username-display").textContent = savedUsername;
    }
});