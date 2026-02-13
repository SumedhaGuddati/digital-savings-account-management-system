<template>
  <Teleport to="body">
    <div v-if="show" class="modal-overlay" @click.self="closeModal">
      <div class="modal-container">
        <div class="modal-header">
          <h2>Terms and Conditions</h2>
          <button class="close-btn" @click="closeModal">✕</button>
        </div>

        <div class="modal-content">
          <div class="terms-document">
            <div class="document-header">
              <h1>ZETARISE DIGITAL BANKING</h1>
              <p class="document-subtitle">Account Services Agreement</p>
              <p class="document-version">Version 2.3 | Effective: Feb 2026</p>
            </div>

            <div class="terms-scroll-area" ref="termsScrollRef" @scroll="handleScroll">
              
              <div class="terms-section">
                <h3>1. Account Tiers & Benefits</h3>
                <p>The following terms apply based on the account tier selected during the application process:</p>
                
                <div class="tier-box">
                  <h4>ZETA BASIC (Everyday Smart)</h4>
                  <ul>
                    <li><strong>Min. Balance:</strong> ₹10,000</li>
                    <li><strong>Interest Rate:</strong> 4.5% p.a.</li>
                    <li><strong>ATM:</strong> 5 Free Withdrawals/month</li>
                    <li><strong>Benefits:</strong> Mobile Banking, Zero Maintenance Fees</li>
                  </ul>
                </div>

                <div class="tier-box">
                  <h4>ZETA PRIME (Prime Advantage)</h4>
                  <ul>
                    <li><strong>Min. Balance:</strong> ₹15,000</li>
                    <li><strong>Interest Rate:</strong> 5.5% p.a.</li>
                    <li><strong>ATM:</strong> 10 Free Withdrawals/month</li>
                    <li><strong>Benefits:</strong> Airport Lounge Access (4/year)</li>
                  </ul>
                </div>

                <div class="tier-box">
                  <h4>ZETA ULTRA (Aurora Elite)</h4>
                  <ul>
                    <li><strong>Min. Balance:</strong> ₹25,000</li>
                    <li><strong>Interest Rate:</strong> 6.5% p.a.</li>
                    <li><strong>ATM:</strong> Unlimited Free Withdrawals</li>
                    <li><strong>Benefits:</strong> Lounge Access (12/year), Personal Relationship Manager</li>
                  </ul>
                </div>
              </div>

              <div class="terms-section">
                <h3>2. Non-Maintenance Charges</h3>
                <p>Failure to maintain the specific Minimum Balance for your tier will attract a penalty:</p>
                <ul>
                  <li><strong>Everyday Smart (Basic):</strong> ₹500 / month</li>
                  <li><strong>Prime Advantage (Prime):</strong> ₹750 / month</li>
                  <li><strong>Aurora Elite (Ultra):</strong> ₹1,000 / month</li>
                </ul>
              </div>

              <div class="terms-section">
                <h3>3. Digital Services</h3>
                <p>All accounts include free access to the ZetaRise mobile app and web dashboard. Passwords and MPINs must not be shared. ZetaRise is not liable for losses due to compromised credentials.</p>
              </div>

              <div class="terms-section">
                <h3>4. Declaration</h3>
                <p>By clicking "Submit", I confirm that I have read the interest rates and minimum balance requirements for my selected account type and agree to abide by them.</p>
              </div>

              <div v-if="!hasScrolledToBottom" class="scroll-hint">
                <span>⬇️ Scroll to read full document</span>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <div class="footer-left">
            <span class="read-only-badge">📄 Digital Contract</span>
          </div>
          <div class="footer-right">
            <button class="btn-secondary" @click="closeModal">Close</button>
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script>
export default {
  name: "TermsAndConditions",
  props: {
    show: {
      type: Boolean,
      default: false
    }
  },
  emits: ['close'],
  data() {
    return {
      hasScrolledToBottom: false
    };
  },
  watch: {
    show(newVal) {
      if (newVal) {
        document.body.style.overflow = 'hidden';
        this.hasScrolledToBottom = false;
      } else {
        document.body.style.overflow = 'auto';
      }
    }
  },
  methods: {
    handleScroll() {
      const el = this.$refs.termsScrollRef;
      if (el) {
        const { scrollTop, scrollHeight, clientHeight } = el;
        const bottomThreshold = 20;
        this.hasScrolledToBottom = scrollHeight - scrollTop - clientHeight < bottomThreshold;
      }
    },
    closeModal() {
      this.$emit('close');
    }
  }
};
</script>

<style scoped>
/* Keeping your existing clean styles, adding simple box style for tiers */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal-container {
  width: 90%;
  max-width: 800px;
  height: 80vh;
  background-color: #ffffff;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  font-family: -apple-system, sans-serif;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #eaeef2;
  background-color: #fafbfc;
}

.modal-header h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #1a2634;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: #5f6b7a;
  cursor: pointer;
}

.modal-content {
  flex: 1;
  overflow: hidden;
  background-color: white;
}

.terms-document {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.document-header {
  padding: 24px;
  border-bottom: 1px solid #edf2f7;
}

.document-header h1 {
  margin: 0;
  font-size: 1.4rem;
  color: #0b1c2e;
}

.document-subtitle {
  margin: 5px 0;
  font-size: 1rem;
  color: #2c3e50;
}

.document-version {
  font-size: 0.8rem;
  color: #7b8a9b;
}

.terms-scroll-area {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
}

.terms-section {
  margin-bottom: 28px;
}

.terms-section h3 {
  margin-bottom: 12px;
  font-size: 1.1rem;
  color: #1e2b39;
}

.terms-section p,
.terms-section li {
  color: #3a4c5c;
  line-height: 1.5;
  margin-bottom: 8px;
}

/* New Tier Box Style */
.tier-box {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.tier-box h4 {
  margin: 0 0 8px 0;
  color: #6366f1;
  font-size: 1rem;
}

.tier-box ul {
  margin: 0;
  padding-left: 20px;
}

.scroll-hint {
  text-align: center;
  padding: 16px;
  border-top: 1px dashed #d0d7de;
  color: #5f6b7a;
}

.modal-footer {
  display: flex;
  justify-content: space-between;
  padding: 16px 24px;
  border-top: 1px solid #eaeef2;
  background-color: #fafbfc;
}

.btn-secondary {
  background: white;
  border: 1px solid #cbd5e0;
  padding: 8px 20px;
  border-radius: 6px;
  cursor: pointer;
}
</style>