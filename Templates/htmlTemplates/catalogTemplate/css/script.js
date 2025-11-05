let orderList = [];

function Item(aId, pId, iName, model, article, iSize, color, cost, amount) {
    this.articleId = aId;
    this.productId = pId;
    this.articleNumber = article;
    this.modelNumber = model;
    this.itemName = iName;
    this.itemSize = iSize;
    this.itemPrice = cost;
    this.itemColor = color;
    this.amount = amount;
}

function Catalog(id, number, date, currId, currDate, currRate) {
    this.id = id;
    this.number = number;
    this.date = date;
    this.currId = currId;
    this.currDate = currDate;
    this.currRate = currRate;
}

function Order(date, catalog, orderList) {
    this.date = date;
    this.catalog = catalog;
    this.orderList = orderList;
}

var formAddToOrder = document.getElementById('add_to_order_form');
var spanAddToOrderForm = document.getElementById("close_add_to_order_form");

spanAddToOrderForm.onclick = function () {
    formAddToOrder.style.display = "none";
};

var formOrder = document.getElementById('order_form');
var spanOrderForm = document.getElementById("close_order_form");

spanOrderForm.onclick = function () {
    formOrder.style.display = "none";
};


window.onclick = function (event) {
    if (event.target == formOrder) {
        formOrder.style.display = "none";
    }
};


var sizesLabel = document.getElementsByClassName("size-item");

for (var i = 0, len = sizesLabel.length; i < len; i++) {
    sizesLabel[i].onclick = function (event) {
        // Подготавливаем данные для заполнения формы добавления к заказу
        var elem = event.currentTarget;
        var size = elem.getElementsByClassName("size-label")[0];
        var modelId = elem.getAttribute("model");
        var product = document.getElementById("product_" + modelId);
        var modelNumber = product.getAttribute("model");
        var articleNumber = product.getAttribute("article");

        var header_ = product.getElementsByClassName("form-header-left")[0];
        var price = elem.getElementsByClassName("price")[0];
        var useColors = elem.getAttribute("colors");

        var header = document.getElementById('pre-order-title');

        header.textContent = header_.innerHTML + "(" + size.innerHTML + ")";

        header.setAttribute("pId", elem.id);
        header.setAttribute("aId", modelId);
        header.setAttribute("iName", header_.innerHTML.trim());
        header.setAttribute("iSize", size.innerHTML);
        header.setAttribute("cost", price.innerHTML.trim());
        header.setAttribute("article", price.innerHTML.trim());
        header.setAttribute("modelNumber", modelNumber);
        header.setAttribute("articleNumber", articleNumber);

        // Копируем цвета модели в модальную форму

        var sourceColors = product.getElementsByClassName("color-list")[0];

        var colors = sourceColors.cloneNode(true);

        var destinationColors = formAddToOrder.getElementsByClassName("order-color-container")[0];
        while (destinationColors.firstChild) {
            destinationColors.removeChild(destinationColors.firstChild);
        }

        // delete unused colors
        var colorSet = useColors.split(",");
        var colorBlocks = colors.children;
        for (var i = 0; i < colorBlocks.length; i++) {
            var colorBlock = colorBlocks[i];
            var color_ = colorBlock.getAttribute("title");
            if (colorSet.indexOf(color_) < 0) {
                colorBlock.style.display = "none";
            }
        }

        destinationColors.appendChild(colors);

        var amount_ = document.getElementById("order_amount");
        amount_.value = "1";

        var colorBoxes = colors.getElementsByClassName("color-box");
        for (var x = 0, len = colorBoxes.length; x < len; x++) {
            if (x === 0) {
                colorBoxes[x].setAttribute("selected", "true");
            }
            colorBoxes[x].onclick = function (event) {
                var form = formAddToOrder.getElementsByClassName("order-color-container")[0];
                var colors_ = form.getElementsByClassName("color-box");
                for (var z = 0, len = colors_.length; z < len; z++) {
                    colors_[z].setAttribute("selected", "false");
                }
                event.currentTarget.setAttribute("selected", "true");
            }
        }
        formAddToOrder.style.display = "block";
    };
}

function amountCalculate() {
    var counter = document.getElementById("order_amount_counter");
    var count = 0;
    for (var x = 0, len = orderList.length; x < len; x++) {
        count += orderList[x].amount;
    }
    counter.innerHTML = count;
}

function add_to_order() {
    var color = "";
    var form = formAddToOrder.getElementsByClassName("order-color-container")[0];
    var colors_ = form.getElementsByClassName("color-box");
    for (var x = 0, len = colors_.length; x < len; x++) {
        if (colors_[x].getAttribute("selected") === "true") {
            color = colors_[x].getAttribute("title");
        }
    }

    var header = document.getElementById('pre-order-title');
    var amount = parseInt(document.getElementById("order_amount").value);
    var found = false;
    for (var x = 0, len = orderList.length; x < len; x++) {
        console.log(orderList[x].productId + " - " + parseInt(header.getAttribute('pId')));
        if (parseInt(orderList[x].productId) === parseInt(header.getAttribute('pId'))) {
            if ((orderList[x].itemColor).trim === color.trim) {
                orderList[x].amount += amount;
                formAddToOrder.style.display = "none";
                found = true;
            }
        }
    }

    if (!found) {
        orderList.push(
            new Item(
                header.getAttribute('aId'),
                header.getAttribute('pId'),
                header.getAttribute('iName'),
                header.getAttribute('modelNumber'),
                header.getAttribute('articleNumber'),
                header.getAttribute('iSize'),
                color,
                header.getAttribute('cost'),
                amount));
    }
    formAddToOrder.style.display = "none";
    amountCalculate();
}

function show_order_form() {
    if (orderList.length < 1) {
        alert("Список заказов пуст.");
        return;
    }
    var form = document.getElementById('order_table');
    while (form.firstChild) {
        form.removeChild(form.firstChild);
    }

    orderList.sort(function (a, b) {
        if (a.itemName > b.itemName) {
            if (a.modelNumber > b.modelNumber) {
                return 1;
            }
            if (a.modelNumber < b.modelNumber) {
                return -1;
            }
            return 0;
        }
        if (a.itemName < b.itemName) {
            return -1;
        }
        return 0;
    });

    createTable(form, orderList);
    formOrder.style.display = "block";
}

function rebuildTable() {
    var form = document.getElementById('order_table');
    while (form.firstChild) {
        form.removeChild(form.firstChild);
    }

    orderList.sort(function (a, b) {
        if (a.itemName > b.itemName) {
            if (a.modelNumber > b.modelNumber) {
                return 1;
            }
            if (a.modelNumber < b.modelNumber) {
                return -1;
            }
            return 0;
        }
        if (a.itemName < b.itemName) {
            return -1;
        }
        return 0;
    });

    createTable(form, orderList);
}

function createTable(parent, list_) {
    var table = document.createElement('table');
    table.className = "order-table";
    var tableBody = document.createElement('tbody');

    var row = document.createElement('tr');
    var cell = document.createElement('th');
    cell.appendChild(document.createTextNode("Наименование"));
    cell.style["width"] = "340px";
    row.appendChild(cell);

    cell = document.createElement('th');
    cell.appendChild(document.createTextNode("Модель"));
    cell.style["width"] = "60px";
    row.appendChild(cell);

    cell = document.createElement('th');
    cell.appendChild(document.createTextNode("Артикул"));
    cell.style["width"] = "150px";
    row.appendChild(cell);

    cell = document.createElement('th');
    cell.appendChild(document.createTextNode("Размер"));
    cell.style["width"] = "150px";
    row.appendChild(cell);

    cell = document.createElement('th');
    cell.appendChild(document.createTextNode("Цвет"));
    cell.style["width"] = "100px";
    row.appendChild(cell);

    cell = document.createElement('th');
    cell.appendChild(document.createTextNode("Цена"));
    cell.style["width"] = "50px";
    row.appendChild(cell);

    cell = document.createElement('th');
    cell.appendChild(document.createTextNode("Количество"));
    cell.style["width"] = "50px";
    row.appendChild(cell);

    cell = document.createElement('th');
    cell.appendChild(document.createTextNode("Сумма"));
    cell.style["width"] = "90px";
    row.appendChild(cell);

    cell = document.createElement('th');
    cell.appendChild(document.createTextNode("-"));
    cell.style["width"] = "10px";
    row.appendChild(cell);

    tableBody.appendChild(row);
    var sum = 0.0;
    var amount = 0;
    list_.forEach((row_) => {
        row = document.createElement('tr');
        row.id = row_.productId + "_" + row_.itemColor;

        cell = document.createElement('td');
        row.appendChild(cell);
        var a_ = document.createElement('a');
        a_.className = "anchor-link";
        //a_.appendChild(document.createTextNode(row_.itemName));
        a_.href = "#product_" + row_.articleId;
        a_.innerHTML = row_.itemName;
        cell.appendChild(a_);


        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(row_.modelNumber));
        row.appendChild(cell);

        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(row_.articleNumber));
        row.appendChild(cell);

        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(row_.itemSize));
        row.appendChild(cell);

        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(row_.itemColor));
        row.appendChild(cell);

        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(row_.itemPrice));
        row.appendChild(cell);

        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(row_.amount));
        row.appendChild(cell);

        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(Number(row_.amount * row_.itemPrice).toFixed(2)));
        row.appendChild(cell);

        cell = document.createElement('td');
        row.appendChild(cell);
        a_ = document.createElement('span');
        a_.appendChild(document.createTextNode('\u2716'));
        cell.appendChild(a_);

        a_.className = "delete-item";
        a_.onclick = function (event) {
            var d = event.currentTarget.parentNode.parentNode;
            var array_ = d.id.split("_");
            var itemId = array_[0];
            var color_ = array_[1];
            console.log(color_ + " - " + itemId);
            var index = -1;
            for (var x = 0, len = orderList.length; x < len; x++) {
                if (parseInt(orderList[x].productId) === parseInt(itemId)) {
                    if ((orderList[x].itemColor).trim === color_.trim) {
                        orderList.splice(x, 1);
                        break;
                    }
                }
            }

            amountCalculate();

            if (orderList.length < 1) {
                formOrder.style.display = "none";

            } else {
                rebuildTable();
            }
        };

        sum += row_.amount * row_.itemPrice;
        amount += row_.amount;
        tableBody.appendChild(row);
    });

    row = document.createElement('tr');

    cell = document.createElement('td');
    cell.appendChild(document.createTextNode("Итого"));
    cell.align = "right";
    cell.colSpan = 6;

    row.appendChild(cell);

    cell = document.createElement('td');
    cell.appendChild(document.createTextNode(amount));
    row.appendChild(cell);

    cell = document.createElement('td');
    cell.appendChild(document.createTextNode(Math.floor(sum * 100) / 100));
    row.appendChild(cell);

    tableBody.appendChild(row);
    table.appendChild(tableBody);
    parent.appendChild(table);
}

function saveOrder() {
    var catalogElement = document.getElementById('catalog-information');

    var catalog = new Catalog(
        catalogElement.getAttribute("catalog-id"),
        catalogElement.getAttribute("catalog-number"),
        catalogElement.getAttribute("catalog-date"),
        catalogElement.getAttribute("currency-id"),
        catalogElement.getAttribute("currency-date"),
        catalogElement.getAttribute("currency-rate"));

    var order = new Order(new Date(), catalog, orderList);

    var js = JSON.stringify(order);
    var pom = document.createElement('a');
    pom.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(js));
    pom.setAttribute('download', "order_.order");
    pom.style.display = 'none';
    document.body.appendChild(pom);
    pom.click();
    document.body.removeChild(pom);
}