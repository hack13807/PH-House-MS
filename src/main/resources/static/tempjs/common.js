var selectedRows = [];
var selectedObjRows = [];
var roomList = [];
/**
 * 勾选记录
 */
$('#table').on('check.bs.table', function (e, row) {
    if ($.inArray(row.rowId, selectedRows) === -1) {
        selectedRows.push(row.rowId);
        selectedObjRows.push(row);
    }
});

/**
 * 取消勾选
 */
$('#table').on('uncheck.bs.table', function (e, row) {
    var index = $.inArray(row.rowId, selectedRows);
    if (index !== -1) {
        selectedRows.splice(index, 1);
        selectedObjRows.splice(index, 1);
    }
});

/**
 * 翻页渲染表格后，加载勾选记录
 */
$('#table').on('post-body.bs.table', function () {
    var rows = $('#table').bootstrapTable('getData');
    for (var i = 0; i < rows.length; i++) {
        if ($.inArray(rows[i].rowId, selectedRows) !== -1) {
            $('#table').bootstrapTable('check', i);
        }
    }
});

/**
 * 页面全选
 */
$('#table').on('check-all.bs.table', function (e, rows) {
    `$.each(rows, function(index, row) {
        if ($.inArray(row.rowId, selectedRows) === -1) {
            selectedRows.push(row.rowId);
            selectedObjRows.push(row);
        }
    });`
});

/**
 * 取消全选，从页面配置中获取当前页的下标范围，再从selectedRows中移除
 */
$('#table').on('uncheck-all.bs.table', function (e) {
    var options = $('#table').bootstrapTable('getOptions');
    var pageSize = options.pageSize;
    var currentPage = options.pageNumber;
    var data = $('#table').bootstrapTable('getData');
    var startIndex = (currentPage - 1) * pageSize;
    var endIndex = startIndex + pageSize;
    var rows = data.slice(startIndex, endIndex);

    $.each(rows, function(index, row) {
        var index = selectedRows.indexOf(row.rowId);
        if (index > -1) {
            selectedRows.splice(index, 1);
            selectedObjRows.splice(index, 1);
        }
    });
});

// 初始化
$(function () {
    // 绑定回车键
    $("body").keydown(function () {
        if (event.keyCode == "13") {	// keyCode=13是回车键
            $('#search').click();
        }
    });
});

function cleanSelectRows() {
    selectedRows = [];
    selectedObjRows = [];
    $('#table').bootstrapTable('uncheckAll')
}

/*列表搜索*/
function search() {
    cleanSelectRows();
    $("#table").bootstrapTable('refresh');
}

/*日期格式化*/
function dateFormatter(value, row) {
    return moment(value).format('YYYY-MM-DD');
}

/*金额格式化*/
function amountFormatter(value) {
    return value + ' 元';
}

/*销毁校验器*/
function resetValidate() {
    if ($("#addOrUpdateform").data('bootstrapValidator')) { // 判断是否存在Validator
      $("#addOrUpdateform").data('bootstrapValidator').destroy(); // 销毁Validator
    }
    initValidate();
}
$('#addOrUpdateModal').on('hidden.bs.modal', function () {
    // 执行校验器重置操作
    resetValidate();
});

/*发起表单校验并提交*/
function validate() {
    $('#addOrUpdateform').bootstrapValidator('validate');
    if ($('#addOrUpdateform').data('bootstrapValidator').isValid()) {
        console.log('表单验证通过');
        // 在此处进行相应的操作
        addOrUpdate();
    }
}
/*初始化房间下拉框*/
function initRoom() {
    $.ajax("/room/roomList", {
        type: 'get',
        dataType: "json",
        success: function (data) {
            roomList = data.data;
            var opts = "";
            for (var index = 0; index < roomList.length; index++) {
                var room = roomList[index];
                opts += "<option value='" + room.id + "'>" + room.number + '房' + "</option>";
            }
// 查询界面
            $("#roomId").append(opts);
            // $("#roomId").selectpicker("refresh");
        }
    });
}

// 根据房间ID获取房间号
function getRoomNumber(roomId) {
    for (var index = 0; index < roomList.length; index++) {
        var room = roomList[index];
        if (String(room.id) === roomId) {
            return room.number;
        }
    }
    return null; // 如果未找到对应的房间号，返回 null 或其他适当的默认值
}