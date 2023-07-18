var memberCache = [];
var valueCache = [];
var hideSelectBoxTask = null;  // 延迟隐藏选择框的任务
/*表格初始化*/
$('#table').bootstrapTable({
    url: '/receipt',
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
//            voLeaseType: document.getElementById("voLeaseType").value,
//            memberName: $("#memberSearch").val(),
//            roomNo: $("#roomSearch").val(),
//            voEffective: $("#effectiveSelect").val()
        }
    },
    responseHandler: function (res) {
        return {total: res.data.total, rows: res.data.rows}
    },
    columns: [
//    {
//        checkbox: true,
//        visible: true
//    },
    {
        field: 'rowId',
        title: '收款单ID',
        visible: false,
    }, {
       field: 'receiptNumber',
       title: '收款单编号',
       align: "center",
   }, {
     field: 'receiptType',
     title: '款项类型',
     align: "center",
    }, {
    field: 'payType',
    title: '支付方式',
    align: "center",
   }, {
     field: 'amount',
     title: '收款金额',
     formatter: amountFormatter,
     align: "center",
 },{
      field: 'bizDate',
      title: '收款日期',
      formatter: dateFormatter,
      align: "center",
  },{
        field: 'leaseNumber',
        title: '租约编号',
        align: "center",
    }, {
        field: 'roomNo',
        title: '房间号',
        align: "center",
    }, {
        field: 'memberName',
        title: '租客',
        align: "center",
    }, {
        field: 'memberIdcard',
        title: '租客身份证号',
        align: "center",
        visible: false,
    },{
        field: 'memberTel',
        title: '租客手机号',
        align: "center",
        visible: false,
    }, {
        field: 'createTime',
        title: '创建时间',
        formatter: dateFormatter,
        align: "center",
    }, {
        field: 'lastUpdateTime',
        title: '最后修改时间',
        formatter: dateFormatter,
        align: "center",
    }],
    cellStyle: function (value, row, index) {
        return {css: {'background-color': '#F3F3F4'}};
    },
    onDblClickRow: function(row, $element) {
      // 点击行触发的操作
      openMadel(row, '查看租约');
      lockAllInfo();
      // 其他操作逻辑...
    },
    onLoadError: function (status, res) {
        var resJson = res.hasOwnProperty('responseJSON') ? res.responseJSON : null;
        var errMsg = resJson && resJson.msg ? resJson.msg : null;
        if (errMsg) swal("获取租约列表失败", errMsg, "error");
    }
});
$('td[data-rowcolor]').attr("style", "background-color:yellow;");

/*添加租约*/
function addLease() {
    $('.modal-title').text("新增收款")
    $('#addOrUpdateModal').modal('show')
    $('#addOrUpdateform')[0].reset()  //重置表单
}

function unlockMemberInfo() {
    $('#memberName').val('').prop('disabled', false);
    $('#tel').val('').prop('disabled', false);
    $('#idCard').val('').prop('disabled', false);
    $('#memberId').val('').prop('disabled', false);
    $('#sex').val('1').prop('disabled', false);

    $("#roomId").prop('disabled', false);
    $("#leaseType").prop('disabled', false);
    $("#unit").prop('disabled', false);
    $("#rentAmount").prop('disabled', false);
    $("#startDate").prop('disabled', false);
    $("#endDate").prop('disabled', false);

    $('#refreshMemberInfo').show();
}

function lockMemberInfo() {
    $('#memberName').prop('disabled', true);
    $('#tel').prop('disabled', true);
    $('#idCard').prop('disabled', true);
    $('#memberId').prop('disabled', true);
    $('#sex').prop('disabled', true);
}
function lockAllInfo() {
    lockMemberInfo();
    $('#sex').prop('disabled', true);
    $("#roomId").prop('disabled', true);
    $("#leaseType").prop('disabled', true);
    $("#unit").prop('disabled', true);
    $("#rentAmount").prop('disabled', true);
    $("#startDate").prop('disabled', true);
    $("#endDate").prop('disabled', true);
}

// 监听输入框的oninput事件
$('#memberName').on('input', function () {
    var inputValue = $(this).val();
    // 发送Ajax请求获取匹配数据并渲染选择框
    // 获取输入框的值
    var inputValue = $('#memberName').val();
    // 获取选择框元素
    var selectBox = $('#selectBox');
    selectBox.empty();
    $.each(memberCache, function (index, item) {
        if (item.name.indexOf(inputValue) !== -1) {
            var name = $('<li>').text(item.name + ' / ' + item.tel);
            selectBox.append(name);
            valueCache.push(item.name + ' / ' + item.tel + ' / ' + item.idCard + ' / ' + item.id + ' / ' + item.sex);
        }
    });
    // 显示选择框
    selectBox.show();
});

// 监听选择框的点击事件
$(document).on('click', '#selectBox li', function () {
    var selectedValue = $(this).text();
//        $('#memberName').val(selectedValue);
    $.each(valueCache, function (index, item) {
        if (item.indexOf(selectedValue) !== -1) {
            var values = item.split(' / ');
            $('#memberName').val(values[0]).prop('disabled', true);
            $('#tel').val(values[1]).prop('disabled', true);
            $('#idCard').val(values[2]).prop('disabled', true);
            $('#memberId').val(values[3]).prop('disabled', true);
            $('#sex').val(values[4]).prop('disabled', true);
            // 回填租客信息后重置校验器
            resetValidate();
        }
    });
    valueCache = [];
    $('#selectBox').hide();
});

// 监听输入框的失去焦点事件
$('#memberName').blur(function () {
    // 延迟隐藏选择框
    hideSelectBoxTask = setTimeout(function () {
        $('#selectBox').hide();
    }, 200);  // 延迟时间可以根据实际情况进行调整
});

// 监听输入框的获得焦点事件
$('#memberName').focus(function () {
    // 取消延迟隐藏选择框的任务
    if (hideSelectBoxTask) {
        clearTimeout(hideSelectBoxTask);
        hideSelectBoxTask = null;
    }
});

function edit() {
    let selecton = $("#table").bootstrapTable('getSelections'); //获取该行数据
        if (selecton.length == 0) {
            swal("请选择需要修改的数据")
            return;
        } else if (selecton.length > 1) {
            swal("请选择单条数据修改")
            return;
        } else if ($("#table").bootstrapTable('getSelections')[0].effective === effectiveMapping['已失效']) {
              swal("失效租约不能修改")
              return;
          }else {
            let row = $("#table").bootstrapTable('getSelections')[0]; //获取该行数据
            openMadel(row,'修改租约');
            lockMemberInfo();
    }
};

function openMadel(row, title){
        // 编辑租约时不允许修改租客信息
                $('#refreshMemberInfo').hide();
                $.ajax({
                    type: "get",
                    url: "/member/queryMember?id=" + row.memberId,
                    dataType: "json",
                    success: function (res) {
                        if (res.code == 200) {
                            let member = res.data[0];
                            $('#addOrUpdateModal').modal('show')
                            // 回填数据，记得回填隐藏的input框的value值为要修改的数据的id主键值
                            $("#memberName").val(member.name);
                            $("#memberId").val(member.id);
                            $("#tel").val(member.tel);
                            $("#sex").val(member.sex);
                            $("#idCard").val(member.idCard);
                        } else {
                            swal("查找租客失败", res.msg + "\n" + res.data, "error")
                        }
                    },
                    error: function (res) {
                        swal("查找租客失败", res.responseJSON.msg, "error")
                    }
                })
                if (row.rowId !== null) {
                    // {# 修改modal框的标题 #}
                    $('.modal-title').text(title + '<' + row.leaseNumber + '>')
                }
                let type;
                if (row.leaseType === '按日租') type = 1;
                if (row.leaseType === '按月租') type = 2;
                if (row.leaseType === '按年租') type = 3;
                $("#id").val(row.rowId);
                $("#roomId").val(row.roomId);
                $("#leaseType").val(type);
                $("#unit").val(row.unit);
                $("#rentAmount").val(row.rentAmount);
                var startDate = new Date(row.startDate);
                var endDate = new Date(row.endDate);
                var formattedStartDate = startDate.toISOString().substring(0, 10);
                var formattedEndDate = endDate.toISOString().substring(0, 10);
                $("#startDate").val(formattedStartDate);
                $("#endDate").val(formattedEndDate);
}

function addOrUpdate() {
    let leaseId = $('#id').val();
    let memberId = $('#memberId').val();
    var title = '';
    var text = '';
    if (!leaseId) {
        if (!memberId) {
            title = '手工录入';
            text = '租客信息为手工录入，系统内若无该租客信息，将会自动创建';
        } else {
            title = '自动填充';
            text = '租客信息为自动填充，系统将为该租客创建新租约';
        }
        swal({
                title: title,
                text: text,
                type: "info",
                showCancelButton: true,
                confirmButtonColor: "#2e6da4",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: false,
                // closeOnCancel: false //取消时不自动关闭弹框
            },
            function (isConfirm) {
                if (isConfirm) {
                    // 新增、修改
                    doAddOrUpdate();
                }
            });
    } else {
        doAddOrUpdate();
    }

}

function doAddOrUpdate() {
    let leaseId = $('#id').val();
    let roomId = $('#roomId').val();
    let data = {
        rowId: leaseId,
        leaseNumber: $('#number').val(),
        // 租客信息
        memberId: $('#memberId').val(),
        memberName: $('#memberName').val(),
        tel: $('#tel').val(),
        sex: $('#sex').val(),
        idCard: $('#idCard').val(),
        // 租赁信息
        roomId: roomId,
        roomNo: getRoomNumber(roomId),
        rentAmount: $('#rentAmount').val(),
        leaseType: $('#leaseType').val(),
        unit: $('#unit').val(),
        startDate: $('#startDate').val(),
        endDate: $('#endDate').val(),
    };
    // {# 如果不存在project_id就是新增 #}
    if (!leaseId) {
        $.ajax({
            type: "POST",
            url: "/lease",
            dataType: "json",
            contentType: "application/json;charset=UTF-8",  // 设置请求头部
            data: JSON.stringify(data),  // 设置请求体
            success: function (res) {
                if (res.code == 200) {
                    swal("新 增", "已建立新租约",
                        "success");
                } else {
                    swal("新增失败", res.msg, "error")
                }
                // {#关闭模态框并清除框内数据，否则下次打开还是上次的数据#}
                $("#table").bootstrapTable('refresh');
                $("#addOrUpdateform")[0].reset();
                $('#id').val('');   // 租客id作为隐藏字段无法通过reset()清除，需要单独处理
                $('#addOrUpdateModal').modal('hide');
            },
            error: function (res) {
                swal("新增失败", res.responseJSON.msg, "error")
            }
        })
    }
    // {# 如果project_id存在就是修改 #}
    else {
        let data = {
            id: leaseId,
            rowId: leaseId,
            leaseNumber: $('#number').val(),
            // 租客信息
            memberId: $('#memberId').val(),
            memberName: $('#memberName').val(),
            tel: $('#tel').val(),
            sex: $('#sex').val(),
            idCard: $('#idCard').val(),
            // 租赁信息
            roomId: roomId,
            roomNo: getRoomNumber(roomId),
            rentAmount: $('#rentAmount').val(),
            leaseType: $('#leaseType').val(),
            unit: $('#unit').val(),
            startDate: $('#startDate').val(),
            endDate: $('#endDate').val(),
        };
        var arr = [];
        data.optType = 1;
        arr.push(data);
        $.ajax({
            type: "PUT",
            url: "/lease", // 待后端提供PUT修改接口
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(arr),  // 设置请求体
            success: function (res) {
                console.log(res);
                if (res.code == 200) {
                    swal("修 改", "租约信息已修改",
                        "success");
                    cleanSelectRows();
                } else {
                    swal("修改失败", res.msg, "error")
                }
                // {#关闭模态框并清除框内数据，否则下次打开还是上次的数据#}
                $("#table").bootstrapTable('refresh');
                $("#addOrUpdateform")[0].reset();
                $('#id').val('');   // 租客id作为隐藏字段无法通过reset()清除，需要单独处理
                $('#addOrUpdateModal').modal('hide');
                $("#table").bootstrapTable('refresh');
            },
            error: function (res) {
                swal("修改失败", res.responseJSON.msg, "error")
            }
        })
    }
}

$('#addOrUpdateModal').on('hidden.bs.modal', function () {
    unlockMemberInfo();
});


function deleteRows() {
    let length = selectedRows.length;
    if (length === 0) {
        swal("请选择要删除的租约")
        return
    }
//    let eff = ''
//    $.each(selectedObjRows, function(index, row) {
//            if (row.effective === 1) {
//                eff += row.leaseNumber
//            }
//        });
//        if(eff != ''){
//         swal("删除失败", '删除前请先将所选租约失效' + "\n" + eff, "error")
//        }
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
                $.ajax("/lease", {
                    type: "delete",
                    dataType: "json",
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(selectedRows),
                    success: function (res) {
                        if (res.code === 200) {
                            swal("删 除", "所选租约已删除",
                                "success");
                            cleanSelectRows();
                            $("#table").bootstrapTable('refresh');
                        } else {
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


<!--  租约信息表单校验规则  -->
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
                },
                tel: {
                    validators: {
                        notEmpty: {
                            message: '手机号不能为空'
                        },
                        regexp: {
                            regexp: /^1[3456789]\d{9}$/,
                            message: '手机号码格式错误'
                        }
                    }
                },
                rentAmount: {
                    validators: {
                        notEmpty: {
                            message: '租金额不能为空'
                        },
                        regexp: {
                            regexp: /^([1-9]\d{0,4}|\d{2,4})(\.\d{1,1})?$/,
                            message: '租金金额格式错误，请重新输入'
                        }
                    }
                },
                unit: {
                    validators: {
                        notEmpty: {
                            message: '租住时长不能为空'
                        },
                        integer: {
                            message: '请输入整数'
                        },
                        callback: {
                            message: '请输入 1 至 1000 范围内的整数',
                            callback: function (input) {
                                var value = parseInt(input);
                                return value >= 1 && value <= 1000;
                            }
                        }
                    }
                },
                startDate: {
                    validators: {
                        notEmpty: {
                            message: '开始日期不能为空'
                        }
                    }
                },
                endDate: {
                    validators: {
                        notEmpty: {
                            message: '结束日期不能为空'
                        }
                    }
                }
            }
        });
    });
}

<!--  手动失效租约  -->
function terminate() {
    let length = selectedRows.length;
    if (length === 0) {
        swal("请选择要失效的租约")
        return
    }
    swal({
            title: "确定失效吗？",
            text: "是否将选中的" + length + "条租约失效",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定失效",
            cancelButtonText: "取消失效",
            closeOnConfirm: false,
            // closeOnCancel: false
        },
        function (isConfirm) {
            if (isConfirm) {
                let arr = [];
                for (var index = 0; index < selectedObjRows.length; index++) {
                    var obj = selectedObjRows[index];
                    if(obj.effective === effectiveMapping["生效中"]) {
                     obj.voEffective = 0;
                     arr.push(obj);
                    }
                }
                if(arr.length === 0) {
                    swal("所选租约已经失效，无需操作")
                    return;
                }
                $.ajax({
                    type: "PUT",
                    url: "/lease", // 待后端提供PUT修改接口
                    dataType: 'json',
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(arr),  // 设置请求体
                    success: function (res) {
                        console.log(res);
                        if (res.code == 200) {
                            swal("失 效", "租约已失效",
                                "success");
                            cleanSelectRows();
                        } else {
                            swal("操作失败", res.msg, "error")
                        }
                        // {#关闭模态框并清除框内数据，否则下次打开还是上次的数据#}
                        $("#table").bootstrapTable('refresh');
                        $("#addOrUpdateform")[0].reset();
                        $('#id').val('');   // 租客id作为隐藏字段无法通过reset()清除，需要单独处理
                        $('#addOrUpdateModal').modal('hide');
                        $("#table").bootstrapTable('refresh');
                    },
                    error: function (res) {
                        swal("操作失败", res.responseJSON.msg, "error")
                    }
                })
            }
        });
}

/*废弃*/
function effectiveRows() {
    let length = selectedRows.length;
    if (length === 0) {
        swal("请选择要生效的租约")
        return
    }

    swal({
            title: "确定生效吗？",
            text: "是否生效选中的" + length + "条租约",
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
                    obj.enableStatus = 1;
                }
                $.ajax("/room", {
                    type: "put",
                    dataType: "json",
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(selectedObjRows),
                    success: function (data) {
                        if (data.code == 200) {
                            swal("启 用", "所选房间已启用",
                                "success");
                            selectedRows = [];
                            selectedObjRows = [];
                            $("#table").bootstrapTable('refresh');
                            $('#table').bootstrapTable('uncheckAll');
                        } else {
                            selectedRows = [];
                            selectedObjRows = [];
                            $('#table').bootstrapTable('uncheckAll');
                            swal("启用失败", data.msg, "error")
                        }
                    },
                    error: function (data) {
                        swal("启用失败", data.responseJSON.msg, "error")
                        selectedRows = [];
                        selectedObjRows = [];
                        $('#table').bootstrapTable('uncheckAll');
                    }
                })

            }
        });
}

// 获取下拉框元素
var dropdown = document.getElementById("voLeaseType");
// 绑定 change 事件
dropdown.addEventListener("change", function () {
    $("#table").bootstrapTable('refresh');
});

$(document).ready(function () {
    // 在页面加载完成后执行的脚本
    initRoom();
    initMemberCache();
    initValidate();
    // 如果带参数跳转，默认过滤条件为全部
    if ($('#memberSearch').val()) {
        $('#effectiveSelect').val('-1')
    }
//    $('#effective').hide();
            $('#delete').hide();
});

/*初始化租客下拉框*/
function initMemberCache() {
    $.ajax("/member/cache", {
        type: 'get',
        dataType: "json",
        success: function (res) {
            memberCache = res.data;
        }
    });
}

// 获取下拉框元素
var effectiveSelect = document.getElementById("effectiveSelect");
// 绑定 change 事件
effectiveSelect.addEventListener("change", function () {
    console.log(effectiveSelect)
    // 获取当前选中的值
    var selectedValue = effectiveSelect.value;
    console.log(selectedValue);
    if (selectedValue === '0') {
//        $('#effective').show();
        $('#delete').show();
        $('#terminate').hide();
        $('#edit').hide();
        $('#new').hide();
        // 清除其余条件
        $('#voLeaseType').val('-1')
        $('#memberSearch').val('')
        $('#roomSearch').val('')
    } else if (selectedValue === '-1') {
//        $('#effective').show();
        $('#delete').show();
        $('#terminate').show();
        $('#edit').show();
        $('#new').show();
    } else {
//        $('#effective').hide();
        $('#delete').hide();
        $('#terminate').show();
        $('#edit').show();
        $('#new').show();
    }
    cleanSelectRows();
    $("#table").bootstrapTable('refresh');
});


// 获取下拉框元素
var effectiveSelect = document.getElementById("leaseType");
// 绑定 change 事件
effectiveSelect.addEventListener("change", function () {
    // 获取当前选中的值
    var selectedValue = effectiveSelect.value;
    if (selectedValue === '1') {
        $('#unitTitle').text('租住时长(天)')
    } else if (selectedValue === '2') {
        $('#unitTitle').text('租住时长(月)')
    } else {
        $('#unitTitle').text('租住时长(年)')
    }
});

function enableRows() {
    let length = selectedRows.length;
    if (length === 0) {
        swal("请选择要生效的租约")
        return
    }
    swal({
        title: "确定生效吗？",
        text: "是否生效选中的" + length + "条租约",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#2e6da4",
        confirmButtonText: "确定生效",
        cancelButtonText: "取消生效",
        closeOnConfirm: false,
        // closeOnCancel: false //取消时不自动关闭弹框
    },
    function (isConfirm) {
        if (isConfirm) {
            let arr = [];
            for (var index = 0; index < selectedObjRows.length; index++) {
                var obj = selectedObjRows[index];
                if(obj.effective !== effectiveMapping["生效中"]) {
                 obj.voEffective = 1;
                 arr.push(obj);
                }
            }
            if(arr.length === 0) {
                swal("所选租约已经生效，无需操作")
                return;
            }
            $.ajax("/lease", {
                type: "put",
                dataType: "json",
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(arr),
                success: function (data) {
                    if (data.code == 200) {
                        swal("生 效", "所选租约已生效",
                            "success");
                            cleanSelectRows();
                    } else {
                        swal("生效失败", data.msg, "error")
                    }
                    $("#table").bootstrapTable('refresh');
                },
                error: function (data) {
                    swal("生效失败", data.responseJSON.msg, "error")
                }
            })
        }
    });
}