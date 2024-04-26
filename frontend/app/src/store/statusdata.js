import { defineStore } from 'pinia';
import { statusService } from '~/services';
import { useNotificationStore } from '~/store/notification';

export const useStatusDataStore = defineStore('statusdata', {
  state: () => ({
    singleStatusData: [],
    allStatusData: [],
    deletedStatusData: undefined,
  }),
  getters: {},
  actions: {
    async fetchGetStatusById(id) {
      try {
        const { data } = await statusService.serviceGetStatusById(id);
        this.singleStatusData = data;
      } catch (error) {
        console.log('Something went wrong. (STOSMDJ#2026)', error); // eslint-disable-line no-console
      }
    },
    async fetchGetAllStatus(includeDeleted = false) {
      try {
        const { data } = await statusService.serviceGetAllStatus(
          includeDeleted
        );
        this.allStatusData = data.data;
      } catch (error) {
        const notificationStore = useNotificationStore();
        notificationStore.addNotification({
          text: error.response.data.message || 'Something went wrong',
          type: error.response.data.status != 200 ? 'error' : 'success',
        });
        console.log('Something went wrong. (STKSDOD#5965)', error); // eslint-disable-line no-console
      }
    },
  },
});
