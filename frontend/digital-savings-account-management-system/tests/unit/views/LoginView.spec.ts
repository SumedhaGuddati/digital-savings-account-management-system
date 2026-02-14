import { mount } from "@vue/test-utils";
import LoginView from "@/views/LoginView.vue";

describe("LoginView DOM rendering", () => {
  it("renders the login email input with bound value", async () => {
    const wrapper = mount(LoginView);

    await wrapper.setData({ loginEmail: "test@example.com" });

    const emailInput = wrapper.find('input[type="email"]');
    expect((emailInput.element as HTMLInputElement).value).toBe("test@example.com");
  });

  it("renders the login password input with bound value", async () => {
    const wrapper = mount(LoginView);

    await wrapper.setData({ loginPassword: "password123" });

    const passwordInput = wrapper.find('input[type="password"]');
    expect((passwordInput.element as HTMLInputElement).value).toBe("password123");
  });

  it("renders the features list in the DOM", () => {
    const wrapper = mount(LoginView);

    const features = wrapper.findAll(".flex.flex-wrap.gap-3 div");
    expect(features).toHaveLength(4); 
    expect(features[0].text()).toContain("6.5% Interest");
    expect(features[1].text()).toContain("Zero Balance");
    expect(features[2].text()).toContain("Lounge Access");
    expect(features[3].text()).toContain("24/7 Support");
  });

  it("shows forgot password modal when clicked", async () => {
    const wrapper = mount(LoginView);

    expect(wrapper.findComponent({ name: "ForgotPassword" }).exists()).toBe(false);

    await wrapper.setData({ forgotModalOpen: true });

    expect(wrapper.findComponent({ name: "ForgotPassword" }).exists()).toBe(true);
  });
});
