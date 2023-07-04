/*表格初始化*/
$('#table').bootstrapTable({
    url: '/member',
    method: 'GET',
    pageNumber: 1,                  //初始化加载第一页，默认第一页
    uniqueId: "rowId",           // 表格唯一键
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
            voStatus: document.getElementById("memberStatus").value,
            memberName: $("#memberSearch").val(),
            roomNo: $("#roomSearch").val()
        }
    },
    responseHandler: function (res) {
        return {total: res.data.total, rows: res.data.rows}
    },
    columns: [{
        checkbox: true,
        visible: true
    }, {
        field: 'rowId',
        title: '租客ID',
        visible: false
    }, {
        field: 'roomId',
        title: '房间ID',
        visible: false
    }, {
        field: 'memberName',
        title: '租客姓名',
        align: "center", //表内容居中
    }, {
        field: 'roomNo',
        title: '房间号',
        align: "center", //表内容居中
        // visible: false
    }, {
        field: 'tel',
        title: '手机号',
        align: "center", //表内容居中
    }, {
        field: 'sex',
        title: '性别',
        align: "center", //表内容居中
    }, {
        field: 'idCard',
        title: '身份证',
        align: "center", //表内容居中
    }, {
        field: 'memberStatus',
        title: '租住状态',
        align: "center", //表内容居中
    }],
    cellStyle: function (value, row, index) {
        return {css: {'background-color': '#F3F3F4'}};
    },
    <!--展开子表-->
    onExpandRow: function (index, row, $detail) {
        $detail.html('<table></table>').find('table').bootstrapTable({
            url: '/lease/getByMemberId',
            method: 'get',
            queryParams: {memberId: row.rowId},
            clickToSelect: true,
            responseHandler: function (res) {
                return {rows: res.data}
            },
            columns: [{
                field: 'leaseId',
                title: '租约ID',
                visible: false,
            }, {
                field: 'leaseType',
                title: '租约类型',
                align: "center",
            }, {
                field: 'unit',
                title: '租约时长',
                align: "center",
            }, {
                field: 'rent',
                title: '租金额',
                formatter: amountFormatter,
                align: "center",
            }, {
                field: 'startDate',
                title: '开始日期',
                formatter: dateFormatter,
                align: "center",
            }, {
                field: 'endDate',
                title: '结束日期',
                formatter: dateFormatter,
                align: "center",
            }],
            onLoadError: function(status, res) {
                swal("获取租约列表失败", res.responseJSON.msg, "error")
            }
        });
    },
    onLoadError: function(status, res) {
        swal("获取租客列表失败", res.responseJSON.msg, "error")
    }
});
$('td[data-rowcolor]').attr("style", "background-color:yellow;");

/*添加租客*/
function addMember() {
    initValidate();
    $('.modal-title').text("新增租客")
    $('#addOrUpdateModal').modal('show')
    $('#addOrUpdateform')[0].reset()  //重置表单
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
        if (row.rowId !== null) {
            // {# 修改modal框的标题 #}
            $('.modal-title').text('修改租客信息')
        }
        $('#addOrUpdateModal').modal('show')
        // 回填数据，记得回填隐藏的input框的value值为要修改的数据的id主键值
        $("#name").val(row.memberName);
        $("#id").val(row.rowId);
        $("#tel").val(row.tel);
        $("#sex").val(row.sex === '男' ? 1 : 2);
        $("#idCard").val(row.idCard);
        $("#roomId").val(row.roomId);
    }
};

function addOrUpdate() {
    let memberId = $('#id').val();
    console.log("memberId的值为：" + memberId)
    var data = {
        rowId: memberId,
        memberName: $('#name').val(),
        tel: $('#tel').val(),
        sex: $('#sex').val(),
        idCard: $('#idCard').val(),
        roomId: $('#roomId').val(),
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
            error: function (res) {
                swal("添加失败", res.responseJSON.msg, "error")
            }
        })
    }
    // {# 如果project_id存在就是修改 #}
    else {
    var arr = [];
        data.optType = 1;
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
            error: function (res) {
                swal("修改失败", res.responseJSON.msg, "error")
            }
        })
    }
}


function deleteRows() {
    let length = selectedRows.length;
    if (length === 0) {
        swal("请选择要删除的租客")
        return
    }
    //    检查租客是否退租
    $.ajax("/member/isTerminate?ids=" + selectedRows, {
        type: "get",
        success: function (res) {
            if (res.code === 200) {
                doDelete();
            } else {
                swal("删除失败", res.msg + "\n" + res.data, "error")
            }
        },
        error: function (res) {
            swal("删除失败", res.responseJSON.msg, "error")
        }
    })
}

function doDelete() {
    swal({
            title: "确定删除吗？",
            text: "是否删除选中的" + length + "条记录",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定删除",
            cancelButtonText: "取消删除",
            closeOnConfirm: false,
            // closeOnCancel: false
        },
        function (isConfirm) {
            if (isConfirm) {
                $.ajax("/member", {
                    type: "delete",
                    dataType: "json",
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(selectedRows),
                    success: function (res) {
                        if (res.code === 200) {
                            swal("删 除", "所选租客记录已删除",
                                "success");
                            selectedRows = [];
                            $("#table").bootstrapTable('refresh');
                        } else {
                            selectedRows = [];
                            swal("删除失败", res.msg, "error")
                        }
                    },
                    error: function (res) {
                        swal("删除失败", res.responseJSON.msg, "error")
                    }
                })

            }
        });
}

function initRoom() {
    $.ajax("/room/roomList", {
        type: 'get',
        dataType: "json",
        success: function (data) {
            var roomList = data.data;
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

<!--  租客信息表单校验规则  -->
function initValidate() {
    $(document).ready(function () {
        $('#addOrUpdateform').bootstrapValidator({
            <!--  excluded: [':disabled', ':hidden', ':not(:visible)', ':empty'],-->
            fields: {
                memberName: {
                    validators: {
                        notEmpty: {
                            message: '姓名不能为空'
                        },
                    }
                },
                idCard: {
                    validators: {
                        notEmpty: {
                            message: '身份证号不能为空'
                        },
                        regexp: {
                            regexp: /^\d{17}[\d|x]|\d{15}$/,
                            message: '请输入正确的身份证号码'
                        }
                    }
                }
            }
        });
    });
}

<!--  租客退租  -->
function terminate() {
    let length = selectedRows.length;
    if (length === 0) {
        swal("请选择要退租的租客")
        return
    }
    swal({
        title: "确定退租吗？",
        text: "是否将选中的" + length + "位租客退租",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "确定退租",
        cancelButtonText: "取消退租",
        closeOnConfirm: false,
        // closeOnCancel: false
    },
    function (isConfirm) {
        if (isConfirm) {
            for (var index = 0; index < selectedObjRows.length; index++) {
                var obj = selectedObjRows[index];
                obj.memberStatus = 2;
                obj.optType = 2;
            }
            $.ajax({
                type: "PUT",
                url: "/member", // 待后端提供PUT修改接口
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(selectedObjRows),  // 设置请求体
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
                error: function (res) {
                    swal("修改失败", res.responseJSON.msg, "error")
                }
            })
        }
    });
}

// 获取下拉框元素
var dropdown = document.getElementById("memberStatus");
// 绑定 change 事件
dropdown.addEventListener("change", function () {
    var terminateBtn = document.getElementById("terminate");
    // 获取当前选中的值
    var selectedValue = dropdown.value;
    if (selectedValue === '2') {
        terminateBtn.classList.add("hidden")
    } else {
        terminateBtn.classList.remove("hidden")
    }
    selectedRows = [];
    $("#table").bootstrapTable('refresh');
});

$(document).ready(function () {
    // 在页面加载完成后执行的脚本
//    var enableBtn = document.getElementById("enable");
//    enableBtn.classList.add("hidden")
    initRoom();
});