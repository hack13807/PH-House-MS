var memberCache = [];
var valueCache = [];
var hideSelectBoxTask = null;  // 延迟隐藏选择框的任务
/*表格初始化*/
$(function(){
// 如果带参数跳转，默认过滤条件为全部
    if ($('#memberSearch').val()) {
        $('#effectiveSelect').val('1')
    }
    $('#table').bootstrapTable({
        url: '/lease',
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
                voLeaseType: document.getElementById("voLeaseType").value,
                memberName: $("#memberSearch").val(),
                roomNo: $("#roomSearch").val(),
                voEffective: $("#effectiveSelect").val()
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
            title: '租约ID',
            visible: false,
        }, {
            field: 'roomId',
            title: '房间ID',
            visible: false,
        }, {
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
            field: 'leaseType',
            title: '租约类型',
            align: "center",
        }, {
            field: 'voUnit',
            title: '租约时长',
            align: "center",
        }, {
            field: 'rentAmount',
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
        }, {
            field: 'effective',
            title: '生效状态',
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
});

$('td[data-rowcolor]').attr("style", "background-color:yellow;");

/*添加租约*/
function addLease() {
    $('.modal-title').text("新增租约")
    $('#addOrUpdateModal').modal('show')
    $('#addOrUpdateform')[0].reset()  //重置表单

    var memberRow1 = document.getElementById("memberRow1");
    memberRow1.style.display = "block";
    // 初始化开始时间
    var startDate = new Date();
    var formattedStartDate = startDate.toISOString().substring(0, 10);
    $("#startDate").val(formattedStartDate);
}

function refreshMemberInfo(key) {
    $('#memberName' + key).val('').prop('readonly', false);
    $('#tel' + key).val('').prop('readonly', false);
    $('#idCard' + key).val('').prop('readonly', false);
    $('#memberId' + key).val('').prop('readonly', false);
    $('#sex' + key).val('1').prop('disabled', false);
}
function unlockMemberInfo(key) {
    $('#addRowBtn').show();
    for(var i = 1; i<=5; i++){
        $('#memberName' + i).val('').prop('readonly', false);
        $('#tel' + i).val('').prop('readonly', false);
        $('#idCard' + i).val('').prop('readonly', false);
        $('#memberId' + i).val('').prop('readonly', false);
        $('#sex' + i).val('1').prop('disabled', false);

        if(i > 0){
            let row = document.getElementById("memberRow"+ i);
            row.style.display = "none";
        }
        $('#refreshMemberInfo'+ i).show();
        $('#deleteInfoRow'+ i).show();

    }
    $("#roomId").prop('disabled', false);
    $("#leaseType").prop('disabled', false);
    $("#unit").prop('readonly', false);
    $("#rentAmount").prop('readonly', false);
    $("#startDate").prop('readonly', false);

    $('#refreshMemberInfo').show();
    $('#submitBtn').show();
}

function lockMemberInfo() {
    for(var i = 1; i<=5; i++){
        $('#memberName' + i).val('').prop('readonly', true);
        $('#tel' + i).val('').prop('readonly', true);
        $('#idCard' + i).val('').prop('readonly', true);
        $('#memberId' + i).val('').prop('readonly', true);
        $('#sex' + i).val('1').prop('disabled', true);
    }
}
function lockAllInfo() {
    lockMemberInfo();
    $("#roomId").prop('disabled', true);
    $("#leaseType").prop('disabled', true);
    $("#unit").prop('readonly', true);
    $("#rentAmount").prop('readonly', true);
    $("#startDate").prop('readonly', true);
    $('#submitBtn').hide();
}

function deleteMemberInfoRow (key) {
    var memberRow = document.getElementById("memberRow" + key);
    $('#memberName' + key).val('').prop('readonly', false);
    $('#tel' + key).val('').prop('readonly', false);
    $('#idCard' + key).val('').prop('readonly', false);
    $('#memberId' + key).val('').prop('readonly', false);
    $('#sex' + key).val('1').prop('disabled', false);
    memberRow.style.display = "none";
}

// 监听输入框的oninput事件
$('#memberName1').on('input', function () {
    doInputMatch('#memberName1', '#selectBox1');
});
$('#memberName2').on('input', function () {
    doInputMatch('#memberName2', '#selectBox2');
});
$('#memberName3').on('input', function () {
    doInputMatch('#memberName3', '#selectBox3');
});
$('#memberName4').on('input', function () {
    doInputMatch('#memberName4', '#selectBox4');
});
$('#memberName5').on('input', function () {
    doInputMatch('#memberName5', '#selectBox5');
});

function doInputMatch(memberName, selectBox) {
    var inputValue = $(memberName).val();
    // 发送Ajax请求获取匹配数据并渲染选择框
    // 获取输入框的值
    var inputValue = $(memberName).val();
    // 获取选择框元素
    var selectBox = $(selectBox);
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
}

// 监听选择框的点击事件
$(document).on('click', '#selectBox1 li', function () {
    var selectedValue = $(this).text();
    selectBoxClick(selectedValue, '1');
});
$(document).on('click', '#selectBox2 li', function () {
    var selectedValue = $(this).text();
    selectBoxClick(selectedValue, '2');
});
$(document).on('click', '#selectBox3 li', function () {
    var selectedValue = $(this).text();
    selectBoxClick(selectedValue, '3');
});
$(document).on('click', '#selectBox4 li', function () {
    var selectedValue = $(this).text();
    selectBoxClick(selectedValue, '4');
});
$(document).on('click', '#selectBox5 li', function () {
    var selectedValue = $(this).text();
    selectBoxClick(selectedValue, '5');
});
function selectBoxClick(selectedValue, no) {
    $.each(valueCache, function (index, item) {
        if (item.indexOf(selectedValue) !== -1) {
            var values = item.split(' / ');
            $('#memberName'+no).val(values[0]).prop('readonly', true);
            $('#tel'+no).val(values[1]).prop('readonly', true);
            $('#idCard'+no).val(values[2]).prop('readonly', true);
            $('#memberId'+no).val(values[3]).prop('readonly', true);
            $('#sex'+no).val(values[4]).prop('disabled', true);
            // 回填租客信息后重置校验器
            resetValidate();
        }
    });
    valueCache = [];
    $('#selectBox').hide();
}

// 监听输入框的失去焦点事件
$('#memberName1').blur(function () {
    inputBlur('#selectBox1');
});
$('#memberName2').blur(function () {
    inputBlur('#selectBox2');
});
$('#memberName3').blur(function () {
    inputBlur('#selectBox3');
});
$('#memberName4').blur(function () {
    inputBlur('#selectBox4');
});
$('#memberName5').blur(function () {
    inputBlur('#selectBox5');
});
function inputBlur(selectBox) {
     // 延迟隐藏选择框
    hideSelectBoxTask = setTimeout(function () {
        $(selectBox).hide();
    }, 200);  // 延迟时间可以根据实际情况进行调整
}

// 监听输入框的获得焦点事件
$('#memberName1').focus(function () {
    inputFocus();
});
$('#memberName2').focus(function () {
    inputFocus();
});
$('#memberName3').focus(function () {
    inputFocus();
});
$('#memberName4').focus(function () {
    inputFocus();
});
$('#memberName5').focus(function () {
    inputFocus();
});
function inputFocus() {
    // 取消延迟隐藏选择框的任务
    if (hideSelectBoxTask) {
        clearTimeout(hideSelectBoxTask);
        hideSelectBoxTask = null;
    }
}

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
//            lockMemberInfo();
    }
};

function openMadel(row, title){
                $.ajax({
                    type: "get",
                    url: "/member/queryMemberByLeaseId?id=" + row.rowId,
                    dataType: "json",
                    success: function (res) {
                        if (res.code == 200) {
                            $('#addOrUpdateModal').modal('show')
                            $.each(res.data, function (index, item) {
                                // 查看租约时不允许修改租客信息
                                if(title === '查看租约'){
                                    $('#refreshMemberInfo'+ (index+1)).hide();
                                    $('#addRowBtn').hide();
                                    $('#deleteInfoRow'+ (index+1)).hide();
                                }
                                let row = document.getElementById("memberRow"+ (index+1));
                                row.style.display = "block";
                                $('#memberName'+(index+1)).val(item.name).prop('readonly', true);
                                $('#tel'+(index+1)).val(item.tel).prop('readonly', true);
                                $('#idCard'+(index+1)).val(item.idCard).prop('readonly', true);
                                $('#memberId'+(index+1)).val(item.id).prop('readonly', true);
                                $('#sex'+(index+1)).val(item.sex).prop('disabled', true);
                            });
                            if(title === '修改租约'){
                                $('#updateRowInfoBtn').show();
                            }
                        } else {
                            swal("查找租客失败", res.msg + "\n" + res.data, "error")
                        }
                    },
                    error: function (res) {
                        swal("查找租客失败", res.responseJSON.error, "error")
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

                // 获取当前选中的值
                if (type === '1') {
                    $('#unitTitle').text('租期(天)')
                } else if (type === '2') {
                    $('#unitTitle').text('租期(月)')
                } else {
                    $('#unitTitle').text('租期(年)')
                }
}

function addOrUpdate() {
    let leaseId = $('#id').val();
    var title = '';
    var text = '';
    if (!leaseId) {
        let memberId1 = $('#memberId1').val();
        var memberRow1 = document.getElementById("memberRow1");
        var memberRow2 = document.getElementById("memberRow2");
        var memberRow3 = document.getElementById("memberRow3");
        var memberRow4 = document.getElementById("memberRow4");
        var memberRow5 = document.getElementById("memberRow5");
        let memberId2 = $('#memberId2').val();
        let memberId3 = $('#memberId3').val();
        let memberId4 = $('#memberId4').val();
        let memberId5 = $('#memberId5').val();
        if ((memberRow1.style.display === "block" && !memberId1)
            || (memberRow2.style.display === "block" && !memberId2)
            || (memberRow3.style.display === "block" && !memberId3)
            || (memberRow4.style.display === "block" && !memberId4)
            || (memberRow5.style.display === "block" && !memberId5)) {
            title = '手工录入';
            text = '部分租客信息为手工录入，系统内若无该租客信息，将会自动创建';
        } else {
            title = '自动填充';
            text = '租客信息全部为自动填充，系统将为这些租客创建新租约';
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
    // 打包租客信息
    let members = packageMemberArrays();
    if(members.length === 0){
        swal('请至少登记一位租客')
        return
    }
    let data = {
        rowId: leaseId,
        leaseNumber: $('#number').val(),
        // 租客信息
        members: members,
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
                        // {#关闭模态框并清除框内数据，否则下次打开还是上次的数据#}
                                        $("#table").bootstrapTable('refresh');
                                        $("#addOrUpdateform")[0].reset();
                                        $('#id').val('');   // 租客id作为隐藏字段无法通过reset()清除，需要单独处理
                                        $('#addOrUpdateModal').modal('hide');
                } else {
                    swal("新增失败", res.msg + '\n' + res.data, "error")
                }
            },
            error: function (res) {
                swal("新增失败", res.responseJSON.msg, "error")
            }
        })
    }
    // {# 如果project_id存在就是修改 #}
    else {
        swal({
                    title: "修改租约",
                    text: "此操作会将原租约失效，并创建新租约，是否继续？",
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
                        let data = {
                                    id: leaseId,
                                    rowId: leaseId,
                                    leaseNumber: $('#number').val(),
                                    // 租客信息
                                    members: members,
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
                                data.optType = 'edit';
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
                });

    }
}

function packageMemberArrays(){
    var memberRow1 = document.getElementById("memberRow1");
    var memberRow2 = document.getElementById("memberRow2");
    var memberRow3 = document.getElementById("memberRow3");
    var memberRow4 = document.getElementById("memberRow4");
    var memberRow5 = document.getElementById("memberRow5");
    let memberId1 = $('#memberId1').val();
    let memberId2 = $('#memberId2').val();
    let memberId3 = $('#memberId3').val();
    let memberId4 = $('#memberId4').val();
    let memberId5 = $('#memberId5').val();

    let members = [];
    // 获取第一组租客信息
    if(memberRow1.style.display === "block") {
        var member1 = {
            rowId: $('#memberId1').val(),
            memberName: document.getElementById('memberName1').value,
            idCard: document.getElementById('idCard1').value,
            tel: document.getElementById('tel1').value,
            sex: document.getElementById('sex1').value
        };
        // 将第一组租客信息添加到数组中
        members.push(member1);
    }

    if(memberRow2.style.display === "block") {
        var member2 = {
            rowId: $('#memberId2').val(),
            memberName: document.getElementById('memberName2').value,
            idCard: document.getElementById('idCard2').value,
            tel: document.getElementById('tel2').value,
            sex: document.getElementById('sex2').value
        };
        members.push(member2);
    }
    if(memberRow3.style.display === "block") {
            var member3 = {
                rowId: $('#memberId3').val(),
                memberName: document.getElementById('memberName3').value,
                idCard: document.getElementById('idCard3').value,
                tel: document.getElementById('tel3').value,
                sex: document.getElementById('sex3').value
            };
            members.push(member3);
        }
    if(memberRow4.style.display === "block") {
        var member4 = {
            rowId: $('#memberId4').val(),
            memberName: document.getElementById('memberName4').value,
            idCard: document.getElementById('idCard4').value,
            tel: document.getElementById('tel4').value,
            sex: document.getElementById('sex4').value
        };
        members.push(member4);
    }
    if(memberRow5.style.display === "block") {
        var member5 = {
            rowId: $('#memberId5').val(),
            memberName: document.getElementById('memberName5').value,
            idCard: document.getElementById('idCard5').value,
            tel: document.getElementById('tel5').value,
            sex: document.getElementById('sex5').value
        };
        members.push(member5);
    }

    return members;
}

$('#addOrUpdateModal').on('hidden.bs.modal', function () {
    unlockMemberInfo();
    $('#updateRowInfoBtn').hide();
});


function deleteRows() {
    let length = selectedRows.length;
    if (length === 0) {
        swal("请选择要删除的租约")
        return
    }
    let eff = ''
    $.each(selectedObjRows, function(index, row) {
            if (row.effective === effectiveMapping['生效中']) {
                eff += row.leaseNumber+'\n'
            }
        });
        if(eff != ''){
         swal("删除失败", '生效中的租约不可以直接删除' + "\n" + eff, "error")
         return;
        }
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

    $('#updateRowInfoBtn').hide();
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
//        $('#voLeaseType').val('-1')
//        $('#memberSearch').val('')
//        $('#roomSearch').val('')
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
var leaseTypeSelect = document.getElementById("leaseType");
// 绑定 change 事件
leaseTypeSelect.addEventListener("change", function () {
    // 获取当前选中的值
    var selectedValue = leaseTypeSelect.value;
    if (selectedValue === '1') {
        $('#unitTitle').text('租期(天)')
        calcDayDate();
    } else if (selectedValue === '2') {
        $('#unitTitle').text('租期(月)')
        calcMonthDate();
    } else {
        $('#unitTitle').text('租期(年)')
        calcYearDate();
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

function addMemberInfoRow() {
    // 获取要判断的div元素
    var memberRow1 = document.getElementById("memberRow1");
    var memberRow2 = document.getElementById("memberRow2");
    var memberRow3 = document.getElementById("memberRow3");
    var memberRow4 = document.getElementById("memberRow4");
    var memberRow5 = document.getElementById("memberRow5");
    // 判断div是否隐藏
    if (memberRow1.style.display === "none") {
        // 显示div
        memberRow1.style.display = "block";
    } else if (memberRow1.style.display === "block" && memberRow2.style.display === "none") {
        memberRow2.style.display = "block";
    } else if (memberRow1.style.display === "block" && memberRow2.style.display === "block" && memberRow3.style.display === "none") {
        memberRow3.style.display = "block";
    } else if (memberRow1.style.display === "block" && memberRow2.style.display === "block" && memberRow3.style.display === "block" && memberRow4.style.display === "none") {
        memberRow4.style.display = "block";
    } else if (memberRow1.style.display === "block" && memberRow2.style.display === "block" && memberRow3.style.display === "block" && memberRow4.style.display === "block"
                && memberRow5.style.display === "none") {
        memberRow5.style.display = "block";
    } else {
        swal('一个房间最多登记五位租客')
    }
}

// 监听输入框的oninput事件
$('#unit').on('input', function () {
    var leaseType = document.getElementById("leaseType").value;
    if(leaseType === '1') {
        calcDayDate();
    }else if (leaseType === '2') {
        calcMonthDate();
    }else if (leaseType === '3') {
        calcYearDate();
    }
});

function calcDayDate() {
        var daysInput = document.getElementById("unit");
        var startInput = document.getElementById("startDate");
        var endInput = document.getElementById("endDate");

        // 获取输入的天数
        var days = parseInt(daysInput.value);
        // 计算开始日期
        if(startInput.value){
            var startDate = new Date(startInput.value);
        }else{
            var startDate = new Date();
            var startYear = startDate.getFullYear();
            var startMonth = (startDate.getMonth() + 1).toString().padStart(2, '0');
            var startDay = startDate.getDate().toString().padStart(2, '0');
            var startDateString = startYear + '-' + startMonth + '-' + startDay;
            startInput.value = startDateString;
        }

        // 在开始日期上增加天数
        startDate.setDate(startDate.getDate() + days);

        // 格式化日期字符串
        var endYear = startDate.getFullYear();
        var endMonth = (startDate.getMonth() + 1).toString().padStart(2, '0');
        var endDay = startDate.getDate().toString().padStart(2, '0');
        var endDateString = endYear + '-' + endMonth + '-' + endDay;

        // 填充控件值
        endInput.value = endDateString;
}

function calcMonthDate() {
        var monthsInput = document.getElementById("unit");
        var startInput = document.getElementById("startDate");
        var endInput = document.getElementById("endDate");

        // 获取输入的天数
        var months = parseInt(monthsInput.value);
        // 计算开始日期
        if(startInput.value){
            var startDate = new Date(startInput.value);
        }else{
            var startDate = new Date();
            var startYear = startDate.getFullYear();
            var startMonth = (startDate.getMonth() + 1).toString().padStart(2, '0');
            var startDay = startDate.getDate().toString().padStart(2, '0');
            var startDateString = startYear + '-' + startMonth + '-' + startDay;
            startInput.value = startDateString;
        }

        // 计算结束日期
        var endDate = new Date(startDate.getFullYear(), startDate.getMonth() + months, startDate.getDate());

        // 格式化日期字符串
        var endYear = endDate.getFullYear();
        var endMonth = (endDate.getMonth() + 1).toString().padStart(2, '0');
        var endDay = endDate.getDate().toString().padStart(2, '0');
        var endDateString = endYear + '-' + endMonth + '-' + endDay;

        // 填充控件值
        endInput.value = endDateString;
}

function calcYearDate() {
        var yearsInput = document.getElementById("unit");
        var startInput = document.getElementById("startDate");
        var endInput = document.getElementById("endDate");

        // 获取输入的天数
        var years = parseInt(yearsInput.value);
        // 计算开始日期
        if(startInput.value){
            var startDate = new Date(startInput.value);
        }else{
            var startDate = new Date();
            var startYear = startDate.getFullYear();
            var startMonth = (startDate.getMonth() + 1).toString().padStart(2, '0');
            var startDay = startDate.getDate().toString().padStart(2, '0');
            var startDateString = startYear + '-' + startMonth + '-' + startDay;
            startInput.value = startDateString;
        }

        // 计算结束日期
        var endDate = new Date(startDate.getFullYear() + years, startDate.getMonth(), startDate.getDate());

        // 格式化日期字符串
        var endYear = endDate.getFullYear();
        var endMonth = (endDate.getMonth() + 1).toString().padStart(2, '0');
        var endDay = endDate.getDate().toString().padStart(2, '0');
        var endDateString = endYear + '-' + endMonth + '-' + endDay;

        // 填充控件值
        endInput.value = endDateString;
}

// 获取下拉框元素
var startDate = document.getElementById("startDate");
// 绑定 change 事件
startDate.addEventListener("change", function () {
    var leaseType = document.getElementById("leaseType").value;
    var unit = document.getElementById("unit").value;
    // 获取当前选中的值
    if (leaseType === '1') {
        calcDayDate();
    } else if (leaseType === '2') {
        calcMonthDate();
    } else {
        calcYearDate();
    }
});

function updateMember(){
    let leaseId = $('#id').val();
    let roomId = $('#roomId').val();
    // 打包租客信息
    let members = packageMemberArrays();

    if(members.length === 0){
        swal('请至少登记一位租客')
        return
    }
    let data = {
        id: leaseId,
        rowId: leaseId,
        leaseNumber: $('#number').val(),
        // 租客信息
        members: members,
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
    data.optType = 'updateMember';
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