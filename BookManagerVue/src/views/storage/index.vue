<template>
  <div class="app-container">
      <!-- 顶部功能 -->
      <div class="filter-container" style="margin-bottom: 15px">
          <!-- 书名输入 -->
          <el-input
              v-model="queryParam.bookname"
              placeholder="书名"
              style="width: 200px"
              class="filter-item"
              @keyup.enter.native="handleFilter"
          />
          <!-- 作者输入 -->
          <el-input
              v-model="queryParam.bookauthor"
              placeholder="作者"
              style="width: 200px"
              class="filter-item"
              @keyup.enter.native="handleFilter"
          />
          <!-- 类型选择 -->
          <el-select
              v-model="queryParam.booktypeid"
              filterable
              placeholder="类型"
              clearable
              class="filter-item"
              style="width: 200px"
          >
              <el-option
                  v-for="item in typeData"
                  :key="item.booktypeid"
                  :label="item.booktypename"
                  :value="item.booktypeid"
              />
          </el-select>
          <!-- 一些按钮 -->
          <br />
          <br />
          <el-button
              v-waves
              style="font-size: 18px"
              class="filter-item"
              type="primary"
              @click="handleFilter"
          >
              搜索
          </el-button>
          <el-button
              v-waves
              style="font-size: 18px"
              class="filter-item"
              type="primary"
              @click="handleFilter"
          >

              显示全部
          </el-button>


      </div>

      <!--弹出框-->
      <el-dialog
          :title="formTitle"
          :visible.sync="dialogFormVisible"
          width="40%"
      >
          <el-row>
              <el-col :span="16">
                  <!--普通表单-->
                  <el-form
                      :model="form"
                      :rules="rules"
                      ref="ruleForm"
                      label-width="80px">

                      <el-form-item label="库存量" prop="capacity">
                          <el-input v-model="form.capacity"></el-input>
                      </el-form-item>


                  </el-form>
              </el-col>


          </el-row>

          <div slot="footer" class="dialog-footer">
              <el-button
                  @click="dialogFormVisible = false"
                  style="font-size: 18px">
              取 消
              </el-button>
              <el-button
                  type="primary"
                  @click="submitForm"
                  style="font-size: 18px">
                  确 定11
              </el-button>
          </div>
      </el-dialog>

      <!--弹出框2-->
      <el-dialog
          title="选择用户"
          :visible.sync="dialogFormVisible2"
          width="400px"
      >
          <el-form :model="form2">
              <el-form-item label="用户名" prop="userid" label-width="80px">
                  <el-select
                      v-model="form2.booktypeid"
                      placeholder="请选择用户"
                  >
                      <el-option
                          v-for="item in userData"
                          :key="item.userid"
                          :label="item.username"
                          :value="item.userid"
                      >
                      </el-option>
                  </el-select>
              </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
              <el-button
                  @click="dialogFormVisible2 = false"
                  style="font-size: 18px"
              >

                  取 消</el-button
              >
              <el-button
                  type="primary"
                  @click="submitForm"
                  style="font-size: 18px">
                  确 定</el-button>
          </div>
      </el-dialog>

      <!--数据表格-->
      <el-table
          ref="multipleTable"
          :data="tableData"
          border
          style="width: 100%"
      >
          <el-table-column fixed type="selection" width="55">
          </el-table-column>
          <el-table-column fixed prop="warehouseid" label="仓库号" width="100">
          </el-table-column>
          <el-table-column prop="bookid" label="图书序号" width="120" sortable>
          </el-table-column>


          <el-table-column
              v-if="roleIsAdmin === false"
              label="图书封面"
              width="155"
          >
              <template slot-scope="scope">
                  <el-image
                      :src="$store.state.settings.baseApi + scope.row.bookimg"
                      style="width: 130px; height: 180px"
                  ></el-image>
              </template>
          </el-table-column>
          <el-table-column
              prop="bookname"
              label="图书名称"
              width="150"
              show-overflow-tooltip
          >
          </el-table-column>


          <el-table-column
              prop="capacity"
              label="库存量"
              width="100"
              show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column
              prop="inputtime"
              label="入库时间"
              min-width="300"
              show-overflow-tooltip>

          </el-table-column>
          <el-table-column label="库存地点" width="100" prop="site">

          </el-table-column>
          <el-table-column
              fixed="right"
              label="操作"
              width="130"
          >
              <template slot-scope="scope">
                  <el-button
                      v-permission="['admin','worker']"
                      @click="handleUpdate(scope.row)"
                      type="primary"
                      style="font-size: 18px">
                      编辑</el-button>



              </template>
          </el-table-column>
      </el-table>

      <!--分页条-->
      <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="queryParam.page"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="queryParam.limit"
          layout="total, sizes, prev, pager, next, jumper"
          :total="recordTotal"
          style="margin-top: 15px"
      >
      </el-pagination>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import permission from "@/directive/permission/index.js"; // 权限判断指令
import waves from "@/directive/waves"; // waves directive

import {
  getCount,
  queryStoragesByPage,
  addStorage,
  deleteBookInfo,
  deleteBookInfos,
  updateStorage,
  queryStorage,
} from "@/api/storage";
import { queryBookTypes } from "@/api/booktype";
import { borrowBook } from "@/api/borrow";
import { queryUsers } from "@/api/user";
import axios from "axios";

export default {
  name: "Storage",
  directives: { waves, permission },
  // 创建后
  created() {
      // 从服务器获取数据表格第一页的信息
      queryStoragesByPage(this.queryParam).then((res) => {
          console.log("首页数据获取成功", res);
          this.tableData = res.data;
          this.recordTotal = res.count;
      });
      // 从服务器获取所有的图书类型
      queryBookTypes().then((res) => {
          console.log("图书类型获取成功", res);
          this.typeData = res;
      });
  },
  mounted() {
      if (this.roleIsAdmin === false) {
          this.queryParam.limit = 5;
          this.handleSizeChange(this.queryParam.limit);
      }
  },
  methods: {
      // 分页大小改变监听
      handleSizeChange(curSize) {
          const params = this.queryParam;
          params.limit = curSize;
          queryStoragesByPage(params).then((res) => {
              console.log("分页数据获取成功", res);
              this.tableData = res.data;
              this.recordTotal = res.count;
          });
      },

      // 点击分页监听方法
      handleCurrentChange(curPage) {
          const params = this.queryParam;
          params.page = curPage;

          queryStoragesByPage(params).then((res) => {
              console.log("分页数据获取成功", res);
              this.tableData = res.data;
              this.recordTotal = res.count;
          });
      },

      // 搜索图书
      handleFilter() {
          this.queryParam.page = 1;
          queryStoragesByPage(this.queryParam).then((res) => {
              if (res.code === 0) {
                  this.tableData = res.data;
                  this.recordTotal = res.count;
              }
          });
      },

      // 显示全部
      handleShowAll() {
          this.queryParam.page = 1;
          this.queryParam.warehouseid = null;
          this.queryParam.bookid = null;
          this.queryParam.bookname = null;
          queryStorage(this.queryParam).then((res) => {
              if (res.code === 0) {
                  this.tableData = res.data;
                  this.recordTotal = res.count;
              }
          });
      },


      // 点击添加记录
      handleCreate() {
          // 从服务器获取所有的图书类型
          queryStorage().then((res) => {
              console.log("图书类型获取成功", res);
              this.typeData = res;
          });
          // 表单是添加状态
          this.formType = 0;
          // 将空数据置入form
          this.form = {
              warehouseid: null,
              capacity: "",
              bookid: "",
              workerid: "1",
              bookname: "",
              site: 0,
              inputtime: "",
          };
          // 显示表单框
          this.dialogFormVisible = true;
      },

      // 点击编辑记录
      handleUpdate(row) {
          // 从服务器获取所有的图书类型
          queryStorage().then((res) => {
              console.log("图书类型获取成功", res);
              this.typeData = res;
          });
          // 表单是编辑状态
          this.formType = 1;
          // 将行数据置入form
          this.form = {
              warehouseid: row.warehouseid,
              capacity: row.capacity,
              bookid: row.bookid,
              workerid: row.workerid,
              bookname: row.bookname,
              site: row.site,
              inputtime: row.inputtime,
          };
          // 显示表单框
          this.dialogFormVisible = true;
      },

      // 添加和更新的提交表单
      submitForm() {
        console.log(this.form)
          // if (this.formType === 0) {
          //     // 添加记录
          //     addBookInfo(this.form).then((res) => {
          //         if (res === 1) {
          //             this.$message.success("添加记录成功");
          //             // 跳转到末尾
          //             getCount().then((res) => {
          //                 this.recordTotal = res;
          //                 this.queryParam.page = 1;
          //                 this.handleCurrentChange(this.queryParam.page);
          //             });
          //         } else {
          //             this.$message.error("添加记录失败");
          //         }
          //         this.dialogFormVisible = false; // 关闭对话框
          //     });
          // } else if (this.formType === 1) {
          //     //更新记录
          //     updateStorage(this.form).then((res) => {
          //         if (res === 1 || res === 0) {
          //             this.$message.success("更新记录成功");
          //             this.handleCurrentChange(this.queryParam.page);
          //         } else {
          //             this.$message.error("更新记录失败");
          //         }
          //         this.dialogFormVisible = false; // 关闭对话框
          //     });
          // }
        const response =  axios.post('http://localhost:9111/BookManager/storage/updateStorage', {
            capacity : this.form.capacity,
            bookid:this.form.bookid
        });
        this.dialogFormVisible = false; // 关闭对话框
        location.reload()
        console.log(response)
      },

    //   // 删除记录
    //   handleDelete(row, index) {
    //       this.$confirm("确定要删除该条记录吗?", "提示", {
    //           confirmButtonText: "确定",
    //           cancelButtonText: "取消",
    //           type: "warning",
    //       }).then(() => {
    //           deleteBookInfo(row).then((res) => {
    //               if (res === 1) {
    //                   this.$message.success("删除记录成功");
    //                   this.tableData.splice(index, 1);
    //                   // 如果删完了，获取上一页
    //                   if (this.tableData.length === 0) {
    //                       this.queryParam.page = this.queryParam.page - 1;
    //                       this.handleCurrentChange(this.queryParam.page);
    //                   }
    //               } else if (res === -1) {
    //                   this.$message.error(
    //                       "该图书存在与读者的借阅信息，请尝试先删除所有的本图书借阅信息再重试"
    //                   );
    //               } else {
    //                   this.$message.error("删除记录失败");
    //               }
    //           });
    //       });
    //   },

    //   // 删除一些
    //   handleDeleteSome() {
    //       this.$confirm("确定要删除这些记录吗?", "提示", {
    //           confirmButtonText: "确定",
    //           cancelButtonText: "取消",
    //           type: "warning",
    //       }).then(() => {
    //           // 获取选中的对象数组
    //           const items = this.$refs.multipleTable.selection;
    //           deleteBookInfos(items).then((res) => {
    //               if (res > 0) {
    //                   this.$message.success("删除" + res + "条记录成功");
    //                   if (this.tableData.length === res) {
    //                       //如果本页内容全部删光了
    //                       //当前页为上一页
    //                       if (this.queryParam.page !== 0) {
    //                           this.queryParam.page = this.queryParam.page - 1;
    //                       }
    //                   }
    //                   // 重载当前页
    //                   this.handleCurrentChange(this.queryParam.page);
    //               } else {
    //                   this.$message.error("删除记录失败");
    //               }
    //           });
    //       });
    //   },
  },
  data() {
      return {
          // 表格数据
          tableData: [],
          // 记录总数
          recordTotal: 0,
          // 图书类型数据
          typeData: [],
          // 用户数据
          userData: [],
          // 查询参数
          queryParam: {
              page: 1,
              limit: 10,
              bookname: null,
              bookauthor: null,
              booktypeid: null,
          },
          // 对话框表单显示
          dialogFormVisible: false,
          dialogFormVisible2: false,
          // 表单类型（添加数据:0,修改数据:1）
          formType: 0,
          // 表单数据
          form: {
              warehouseid: null,
              capacity: "",
              bookid: 0,
              workerid: 1,
              bookname: "",
              site: 0,
              inputtime: "",
          },
          form2: {
              userid: 1,
              bookid: 1,
          },
          rules: {
              bookname: [
                  {
                      required: true,
                      message: "请输入图书名称",
                      trigger: "blur",
                  },
              ],
              bookauthor: [
                  { required: true, message: "请输入作者", trigger: "blur" },
              ],
              bookprice: [
                  { required: true, message: "请输入价格", trigger: "blur" },
              ],
              booktypeid: [
                  { required: true, message: "请选择类型", trigger: "blur" },
              ],
              bookdesc: [
                  { required: true, message: "请输入描述", trigger: "blur" },
              ],
              isborrowed: [
                  { required: true, message: "请选择状态", trigger: "blur" },
              ],
          },
      };
  },
  computed: {
      // 获得user信息
      ...mapGetters(["id", "name", "roles"]),
      // 通过表单类型计算表单标题
      formTitle() {
          return this.formType === 0 ? "添加记录" : "修改记录";
      },
      roleIsAdmin() {
          if (this.roles[0] === "admin") return true;
          else if(this.roles[0] === "worker") return true;
          else return false;
      },
  },
};
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 150px;
  height: 200px;
  display: block;
}
</style>
