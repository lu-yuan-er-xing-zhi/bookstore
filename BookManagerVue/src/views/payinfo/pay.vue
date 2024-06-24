
  <!-- SimplePayPage.vue -->
  <template>
    <div class="payment-container">
      <h1>支付</h1>
      <form @submit.prevent="submitPayment">
        <div>
          <label for="paymentMethod">支付方式:
          <select v-model="selectedPaymentMethod" id="paymentMethod">
            <option value="wechat">微信支付</option>
            <option value="alipay">支付宝</option>
            <option value="bankCard">银行卡</option>
          </select>
          </label>
        </div>

        <div v-if="selectedPaymentMethod === 'bankCard'">
          <label for="cardNumber">卡号:</label>
          <input type="text" v-model="cardNumber" id="cardNumber" required>
        </div>
        <button type="submit" >确认支付</button>
      </form>
    </div>
  </template>

<script>
export default {
  data() {
    return {
      selectedPaymentMethod: 'wechat',
      amount: '',
      cardNumber: ''
    };
  },
  methods: {
    submitPayment() {
      alert('支付成功');
      this.$router.push({ name: 'Borrow' });
      // 清空表单
      this.amount = '';
      if (this.selectedPaymentMethod === 'bankCard') {
        this.cardNumber = '';
      }
    },

  }
};
</script>
  <style scoped>
  .payment-container {
    width: 300px;
    margin: 50px auto;
    padding: 30px;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    background-color: #fff;
  }

  h1 {
    font-size: 24px;
    text-align: center;
    margin-bottom: 20px;
  }

  label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
  }

  select, input {
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
    outline: none;
    transition: border-color 0.3s ease;
  }

  select:focus, input:focus {
    border-color: #007bff;
  }

  button {
    padding: 10px;
    width: 100%;
    border: none;
    border-radius: 5px;
    background-color: #007bff;
    color: #fff;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  button:hover {
    background-color: #0056b3;
  }

  button:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
  </style>
