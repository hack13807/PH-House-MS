/*表格初始化*/
$('#table').bootstrapTable({
    url: '/room',
    method: 'GET',
    pageNumber: 1,                  //初始化加载第一页，默认第一页
    uniqueId: "roomId",           // 表格唯一键
    pagination: true,               //是否显示分页（*）
    sidePagination: "server",       //分页方式：client客户端分页，server服务端分页（*）
    pageSize: 10,                       //每页的记录行数（*）
    pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
    contentType: 'application/json',
    toolbar: '#toolbar',
    showRefresh: true,   //是否显示刷新按钮
    showToggle: true,    //是否显示详细视图和列表视图的切换按钮
    showColumns: true,   //选择要显示的列
    striped: true,      //是否显示行间隔色
    toolbarAlign: 'left',//工具栏的位置
    clickToSelect: true,    //是否启用点击选中行
    cardView: false,     //是否显示详细视图
    detailView: true,   //是否显示父子表
    locale: 'zh-CN',    //配置中文汉化包
    maintainSelected: true, //翻页时保留已选中的行的状态
    queryParams: function (params) {    //传递参数（*）
        return {
            offset: params.offset,
            limit: params.limit,
            voStatus: document.getElementById("roomStatus").value,
            roomNo: $("#roomSearch").val(),
        }
    },
    responseHandler: function (res) {
        return {total: res.data.total, rows: res.data.rows}
    },
    columns: [{
        checkbox: true,
        visible: true
    }, {
        field: 'roomId',
        title: '房间ID',
        visible: false
    }, {

        field: 'roomNo',
        title: '房间号',
        align: "center", //表内容居中
    }, {
        field: 'memberName',
        title: '租客姓名',
        align: "center", //表内容居中
        // visible: false
    }, {
        field: 'roomStatus',
        title: '房间状态',
        align: "center", //表内容居中
    }, {
        field: 'roomDesc',
        title: '房间描述',
        align: "center", //表内容居中
    }],
    cellStyle: function (value, row, index) {
        return {css: {'background-color': '#F3F3F4'}};
    },
    onLoadError: function (status, res) {
        swal("获取房间列表失败", res.responseJSON.msg, "error")
    }
});

/*新增房间*/
function addRoom() {
    initValidate();
    $('.modal-title').text("新增租客")
    $('#addOrUpdateModal').modal('show')
    $('#addOrUpdateform')[0].reset()  //重置表单
}

/*删除房间*/
function disableRows() {
    let length = selectedRows.length;
    if (length === 0) {
        swal("请选择要禁用的房间")
        return
    }
    //    检查房间是否有租客
    $.ajax("/room/isInUse?ids=" + selectedRows, {
        type: "get",
        success: function (res) {
            if (res.code == 200) {
                doDisable();
            } else {
                swal("禁用失败", res.msg + "\n" + res.data, "error")
            }
        },
        error: function (res) {
            swal("禁用失败", res.responseJSON.msg, "error")
        }
    })
}

function addOrUpdate() {
    let roomId = $('#id').val();
    console.log("roomId的值为：" + memberId)
    var data = {
        rowId: roomId,
        roomNo: $('#roomNo').val(),
        roomDesc: $('#roomDesc').val(),
        roomStatus: $('#status').val()
    };
    // {# 如果不存在project_id就是新增 #}
    if (!memberId) {
        $.ajax({
            type: "POST",
            url: "/member",
            dataType: "json",
            contentType: "application/json;charset=UTF-8",  // 设置请求头部
            data: JSON.stringify(data),  // 设置请求体
            success: function (res) {
                if (res.code == 200) {
                    swal("新 增", "租客记录已添加",
                        "success");
                } else {
                    swal("添加失败", res.msg, "error")
                }
                // {#关闭模态框并清除框内数据，否则下次打开还是上次的数据#}
                $("#table").bootstrapTable('refresh');
                $("#addOrUpdateform")[0].reset();
                $('#id').val('');   // 租客id作为隐藏字段无法通过reset()清除，需要单独处理
                $('#addOrUpdateModal').modal('hide');
                $("#mytab").bootstrapTable('refresh');
            },
            error: function (data) {
                swal("添加失败", res.responseJSON.msg, "error")
            }
        })
    }
    // {# 如果project_id存在就是修改 #}
    else {
    var arr = [];
    arr.push(data);
        $.ajax({
            type: "PUT",
            url: "/member", // 待后端提供PUT修改接口
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(arr),  // 设置请求体
            success: function (res) {
                console.log(res);
                if (res.code == 200) {
                    swal("修 改", "租客信息已修改",
                        "success");
                } else {
                    swal("修改失败", res.msg, "error")
                }
                // {#关闭模态框并清除框内数据，否则下次打开还是上次的数据#}
                $("#table").bootstrapTable('refresh');
                $("#addOrUpdateform")[0].reset();
                $('#id').val('');   // 租客id作为隐藏字段无法通过reset()清除，需要单独处理
                $('#addOrUpdateModal').modal('hide');
                $("#mytab").bootstrapTable('refresh');
            },
            error: function () {
                swal("修改失败", res.responseJSON.msg, "error")
            }
        })
    }
}

function doDisable() {
    swal({
            title: "确定禁用吗？",
            text: "是否禁用选中的" + length + "条记录",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定禁用",
            cancelButtonText: "取消禁用",
            closeOnConfirm: false,
            // closeOnCancel: false //取消时不自动关闭弹框
        },
        function (isConfirm) {
            if (isConfirm) {
                for (var index = 0; index < selectedObjRows.length; index++) {
                    var obj = selectedObjRows[index];
                    obj.memberStatus = 0;
                }
                $.ajax("/room", {
                    type: "delete",
                    dataType: "json",
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(selectedRows),
                    success: function (data) {
                        if (data.code == 200) {
                            swal("禁 用", "所选房间已禁用",
                                "success");
                            selectedRows = [];
                            $("#table").bootstrapTable('refresh');
                        } else {
                            selectedRows = [];
                            swal("禁用失败", data.msg, "error")
                        }
                    },
                    error: function (data) {
                        swal("禁用失败", data.responseJSON.msg, "error")
                    }
                })

            }
        });
}

function enableRows() {
    let length = selectedRows.length;
    if (length === 0) {
        swal("请选择要启用的房间")
        return
    }
    swal({
        title: "确定启用吗？",
        text: "是否启用选中的" + length + "个房间",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#2e6da4",
        confirmButtonText: "确定启用",
        cancelButtonText: "取消启用",
        closeOnConfirm: false,
        // closeOnCancel: false //取消时不自动关闭弹框
    },
    function (isConfirm) {
        if (isConfirm) {
            for (var index = 0; index < selectedObjRows.length; index++) {
                var obj = selectedObjRows[index];
                obj.memberStatus = 2;
            }
            $.ajax("/room", {
                type: "put",
                dataType: "json",
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(selectedRows),
                success: function (data) {
                    if (data.code == 200) {
                        swal("启 用", "所选房间已启用",
                            "success");
                        selectedRows = [];
                        $("#table").bootstrapTable('refresh');
                    } else {
                        selectedRows = [];
                        swal("启用失败", data.msg, "error")
                    }
                },
                error: function (data) {
                    swal("启用失败", data.responseJSON.msg, "error")
                }
            })

        }
    });
}

function edit() {
    initValidate();
    let selecton = $("#table").bootstrapTable('getSelections'); //获取该行数据
    if (selecton.length == 0) {
        swal("请选择需要修改的数据")
        return;
    } else if (selecton.length > 1) {
        swal("请选择单条数据修改")
        return;
    } else {
        let row = $("#table").bootstrapTable('getSelections')[0]; //获取该行数据
        if (row.id !== null) {
            // {# 修改modal框的标题 #}
            $('.modal-title').text('修改租客信息')
        }
        $('#addOrUpdateModal').modal('show')
        // 回填数据，记得回填隐藏的input框的value值为要修改的数据的id主键值
        $("#id").val(row.rowId);
        $("#roomNo").val(row.roomNo);
        $("#roomDesc").val(row.roomDesc);
    }
};

<!--  房间信息表单校验规则  -->
function initValidate() {
    $(document).ready(function () {
        $('#addOrUpdateform').bootstrapValidator({
            <!--  excluded: [':disabled', ':hidden', ':not(:visible)', ':empty'],-->
            fields: {
                roomNo: {
                    validators: {
                        notEmpty: {
                            message: '房间号不能为空'
                        },
                    }
                }
            }
        });
    });
}

// 获取下拉框元素
var dropdown = document.getElementById("roomStatus");
// 绑定 change 事件
dropdown.addEventListener("change", function () {
    var enableBtn = document.getElementById("enable");
    var disableBtn = document.getElementById("disable");
    // 获取当前选中的值
    var selectedValue = dropdown.value;
    if (selectedValue === '0') {
        enableBtn.classList.remove("hidden")
        disableBtn.classList.add("hidden")
    } else {
        enableBtn.classList.add("hidden")
        disableBtn.classList.remove("hidden")
    }
    selectedRows = [];
    $("#table").bootstrapTable('refresh');
});

$(document).ready(function () {
    // 在页面加载完成后执行的脚本
    var enableBtn = document.getElementById("enable");
    enableBtn.classList.add("hidden")
});