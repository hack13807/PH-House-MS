/*表格初始化*/
    $('#table').bootstrapTable({
        url: '/member/data',
        method: 'post',
        pageNumber: 1,                  //初始化加载第一页，默认第一页
        uniqueId: "memberId",           // 表格唯一键
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
        locale: 'zh-CN',
        queryParams: function (params) {    //传递参数（*）
            return {
                offset: params.offset,
                limit: params.limit,
                memberStatus: document.getElementById("memberStatus").value,
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
            field: 'memberId',
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
        cellStyle: function (value, row, index){return {css:{'background-color':'#F3F3F4'}};
        },
        <!--展开子表-->
        onExpandRow: function (index, row, $detail) {
            $detail.html('<table></table>').find('table').bootstrapTable({
                url: '/lease',
                method: 'get',
                queryParams: {memberId: row.memberId},
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
                }]
            });
        }
    });
    $('td[data-rowcolor]').attr("style", "background-color:yellow;");
    /*添加租客*/
    function addMember() {
        initRoom();
        $('.modal-title').text("新增租客")
        $('#addOrUpdateModal').modal('show')
        $('#addOrUpdateform')[0].reset()  //重置表单
    }

    function edit() {
        initRoom();
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
            $("#memberName").val(row.memberName);
            $("#id").val(row.memberId);
            $("#tel").val(row.tel);
            $("#sex").val(row.sex === '男' ? 1 : 2);
            $("#idCard").val(row.idCard);
            $("#roomId").val(row.roomId);
        }
    };

     function amountFormatter(value) {
        return value + ' 元';
    }
    function dateFormatter(value, row) {
        return moment(value).format('YYYY-MM-DD');
    }

    function addOrUpdate() {
        let memberId = $('#memberId').val();
        console.log("memberId的值为：" + memberId)
        var data = {
            memberName: $('#memberName').val(),
            tel: $('#tel').val(),
            sex: $('#sex').val(),
            idCard: $('#idCard').val(),
            roomId: $('#roomId').val()
        };
        // {# 如果不存在project_id就是新增 #}
        if (!memberId) {
            $.ajax({
                type: "POST",
                url: "member/insert",
                dataType: "json",
                contentType: "application/json;charset=UTF-8",  // 设置请求头部
                data: JSON.stringify(data),  // 设置请求体
                success: function (res_data) {
                    console.log(res_data)
                    // {#关闭模态框并清除框内数据，否则下次打开还是上次的数据#}
                    $("#addOrUpdateform")[0].reset();
                    $('#addOrUpdateModal').modal('hide');
                    $("#mytab").bootstrapTable('refresh');
                }
            })
        }
        // {# 如果project_id存在就是修改 #}
        else {
            $.ajax({
                type: "PUT",
                dataType: "json",
                url: "room/roomNoList", // 待后端提供PUT修改接口
                // data: $('#addOrUpdateform').serialize(),

                success: function (data) {
                    console.log(data);
                    if (data.code == 200) {
                        toastr.success("修改成功");
                        $("#addOrUpdateform")[0].reset();
                        $('#project_id').val("")
                        $('#addOrUpdateModal').modal('hide');
                        $("#mytab").bootstrapTable('refresh');
                    } else {
                        toastr.warning('请填写所有数据');
                    }
                },
                error: function () {
                    toastr.warning("修改失败");
                }
            })
        }
    }


    function searchMember() {
        $("#table").bootstrapTable('refresh');
    }

    function deleteRows() {
        var rows = $("#table").bootstrapTable('getSelections');
        let length = rows.length;
        if (length === 0) {
            swal("请选择要删除的租客")
            return
        }
        swal({
                title: "确定删除吗？",
                text: "是否删除选中的"+ length +"条记录",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定删除",
                cancelButtonText: "取消删除",
                closeOnConfirm: false,
                // closeOnCancel: false
            },
            function(isConfirm){
                if (isConfirm) {
                    var arr = [];
                    $.each(rows, function (i, e) {
                        arr.push(e.memberId);
                    });
                    $.ajax("/member/deleteData", {
                        type: "get",
                        dataType: "json",
                        data: {
                            ids: arr
                        },
                        success: function (data) {
                            if (data.code == 200) {
                                swal("删除", "所选租客记录已删除",
                                    "success");
                                $("#table").bootstrapTable('refresh');
                            } else {
                                swal("删除失败", data.msg, "error")
                            }
                        },
                        error: function (data) {
                            swal("删除失败", data.responseJSON.msg, "error")
                        }
                    })

                }
            });
    }
    function initRoom(){
        $.ajax("/room/roomNoList", {
            type : 'get',
            dataType : "json",
            success : function(data) {
                var roomList = data.data;
                var opts = "";
                for( var index = 0; index < roomList.length; index++ ){
                    var room = roomList[index];
                    opts += "<option value='"+room.id+"'>"+room.number+'房'+"</option>";
                }
// 查询界面
                $("#roomId").append(opts);
                // $("#roomId").selectpicker("refresh");
            }
        });
    }

<!--  租客信息表单校验规则  -->
$(document).ready(function() {
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
        }
      }
    }
  });
});

function validate() {
  $('#addOrUpdateform').bootstrapValidator('validate');
  if ($('#addOrUpdateform').data('bootstrapValidator').isValid()) {
    console.log('表单验证通过');
    // 在此处进行相应的操作
    addOrUpdate();
  } else {
    console.log('表单验证失败');
    console.log('111')
  }
}