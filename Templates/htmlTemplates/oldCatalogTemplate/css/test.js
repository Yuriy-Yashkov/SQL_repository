let orderList = [];

function Item(aId, pId, iName, iSize, cost, color, amount) {
    this.aId = aId;
    this.pId = pId;
    this.iName = iName;
    this.iSize = iSize;
    this.cost = cost;
    this.color = color;
    this.amount = amount;
}

var formAddToOrder = document.getElementById('add_to_order_form');
var spanAddToOrderForm = document.getElementById("close_add_to_order_form");

var formOrder = document.getElementById('order_form');
var spanOrderForm = document.getElementById("close_order_form");

spanAddToOrderForm.onclick = function () {
    formAddToOrder.style.display = "none";
};

spanOrderForm.onclick = function () {
    formOrder.style.display = "none";
};


window.onclick = function (event) {
    if (event.target == formOrder) {
        formOrder.style.display = "none";
    }
};

function show_order_form() {
    var form = document.getElementById('order_table');
    while (form.firstChild) {
        form.removeChild(form.firstChild);
    }

    createTable(form, orderList);
    formOrder.style.display = "block";
}

function createTable(parent, list_) {
    var table = document.createElement('table');
    table.className = "specification-table";
    var tableBody = document.createElement('tbody');

    var row = document.createElement('tr');
    row.className = "order-table-header";

    var cell = document.createElement('th');
    cell.appendChild(document.createTextNode("Наименование"));
    cell.style["width"] = "250px";
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
    cell.style["width"] = "50px";
    row.appendChild(cell);

    tableBody.appendChild(row);
    var sum = 0;
    var amount = 0;
    list_.forEach((row_)=> {
        row = document.createElement('tr');
        row.className = "order-table-row";

        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(row_.iName));
        row.appendChild(cell);

        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(row_.iSize));
        row.appendChild(cell);

        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(row_.color));
        row.appendChild(cell);

        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(row_.cost));
        row.appendChild(cell);

        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(row_.amount));
        row.appendChild(cell);

        cell = document.createElement('td');
        cell.appendChild(document.createTextNode(row_.amount * row_.cost));
        row.appendChild(cell);

        sum += row_.amount * row_.cost;
        amount +=row_.amount ;

        tableBody.appendChild(row);
    });

    row = document.createElement('tr');
    row.className = "order-table-header";

    cell = document.createElement('td');
    cell.appendChild(document.createTextNode("Итого"));
    cell.align = "right";
    cell.colSpan = 4;

    row.appendChild(cell);

    cell = document.createElement('td');
    cell.appendChild(document.createTextNode(amount));
    row.appendChild(cell);

    cell = document.createElement('td');
    cell.appendChild(document.createTextNode(sum));
    row.appendChild(cell);

    tableBody.appendChild(row);
    table.appendChild(tableBody);
    parent.appendChild(table);
}

function add_to_order_form(btn) {
    var idName = btn.getAttribute("id");
    // SDID
    var sd_id = idName.substring(8, idName.length);
    //KLDID
    var kld_id = document.getElementById('size_' + sd_id).getAttribute('value');
    var header = document.getElementById('modal_header_text');
    // ПОлучаем наименование изделия
    var item_name = document.getElementById('item_name_' + kld_id).lastChild.textContent;
    var item_model = document.getElementById('item_model_' + kld_id).lastChild.textContent;
    var item_size = document.getElementById('size_' + sd_id).lastChild.textContent;
    var item_cost = document.getElementById('cost_' + sd_id).lastChild.textContent;

    header.textContent = item_name + " , модель " + item_model + "(" + item_size + ")";

    header.setAttribute("pId", sd_id);
    header.setAttribute("aId", kld_id);
    header.setAttribute("iName", item_name);
    header.setAttribute("iSize", item_size);
    header.setAttribute("cost", item_cost);

    var cbColor = document.getElementById("order_color");
    cbColor.selectedIndex = "0";

    var amount = document.getElementById("order_amount");
    amount.value = 1;

    formAddToOrder.style.display = "block";
}

function add_to_order() {
    var header = document.getElementById('modal_header_text');
    var cbColor = document.getElementById("order_color");
    var color = cbColor.options[cbColor.selectedIndex].text;
    var amount = parseInt(document.getElementById("order_amount").value);

    orderList.push(
        new Item(
            header.getAttribute('aId'),
            header.getAttribute('pId'),
            header.getAttribute('iName'),
            header.getAttribute('iSize'),
            header.getAttribute('cost'),
            color,
            amount));
    formAddToOrder.style.display = "none";
}

function saveOrder() {
    var js = JSON.stringify(orderList);
    var pom = document.createElement('a');
    pom.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(js));
    pom.setAttribute('download', "order.txt");
    pom.style.display = 'none';
    document.body.appendChild(pom);
    pom.click();
    document.body.removeChild(pom);
}

