import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref('mock-token')
  const userInfo = ref({
    id: 1,
    username: 'admin',
    realName: '张三',
    phone: '13800138000',
    village: '罗卜田乡',
    role: 'VILLAGER',
    roleCode: 'VILLAGER',
    isPartyMember: '是',
    partyType: '正式党员',
    joinPartyDate: '2010-07-01',
    partyBranch: '第一党支部',
    partyScore: 95,
    duty: '村民',
    manageBusiness: '',
    startWorkDate: '',
    gridArea: '',
    idCard: '430101**********1234',
    address: '罗卜田乡1组',
    emergencyName: '张四',
    emergencyPhone: '139001394567',
    createTime: '2026-07-01T00:00:00',
    lastLoginTime: '2026-07-20T12:00:00'
  })
  const permissions = ref([])
  const currentRole = ref('VILLAGER')

  const ROLES = {
    ADMIN: 'ADMIN',
    VILLAGE_OFFICIAL: 'VILLAGE_OFFICIAL',
    RESIDENT_OFFICIAL: 'RESIDENT_OFFICIAL',
    VILLAGER: 'VILLAGER',
    PARTY_MEMBER: 'PARTY_MEMBER'
  }

  const roleNames = {
    [ROLES.ADMIN]: '管理员',
    [ROLES.VILLAGE_OFFICIAL]: '村干部',
    [ROLES.RESIDENT_OFFICIAL]: '驻村干部',
    [ROLES.VILLAGER]: '村民',
    [ROLES.PARTY_MEMBER]: '党员'
  }

  const setToken = (newToken) => {
    token.value = newToken
  }

  const setUserInfo = (info) => {
    userInfo.value = info
    if (info?.roleCode) {
      setCurrentRole(info.roleCode)
    }
  }

  const setPermissions = (perms) => {
    permissions.value = perms
  }

  const setCurrentRole = (role) => {
    currentRole.value = role
  }

  const logout = () => {
    token.value = 'mock-token'
    userInfo.value = {
      id: 1,
      username: 'admin',
      realName: '张三',
      phone: '13800138000',
      village: '罗卜田乡',
      role: 'VILLAGER',
      roleCode: 'VILLAGER',
      isPartyMember: '是',
      partyType: '正式党员',
      joinPartyDate: '2010-07-01',
      partyBranch: '第一党支部',
      partyScore: 95,
      duty: '村民',
      manageBusiness: '',
      startWorkDate: '',
      gridArea: '',
      idCard: '430101**********1234',
      address: '罗卜田乡1组',
      emergencyName: '张四',
      emergencyPhone: '139001394567',
      createTime: '2026-07-01T00:00:00',
      lastLoginTime: '2026-07-20T12:00:00'
    }
    permissions.value = []
    currentRole.value = 'VILLAGER'
  }

  const isAuthenticated = computed(() => {
    return true
  })

  const isAdmin = computed(() => {
    return userInfo.value && userInfo.value.role === ROLES.ADMIN
  })

  const isOfficial = computed(() => {
    return userInfo.value && (
      userInfo.value.role === ROLES.VILLAGE_OFFICIAL ||
      userInfo.value.role === ROLES.RESIDENT_OFFICIAL ||
      userInfo.value.role === ROLES.ADMIN
    )
  })

  const isVillageOfficial = computed(() => {
    return userInfo.value && userInfo.value.role === ROLES.VILLAGE_OFFICIAL
  })

  const isResidentOfficial = computed(() => {
    return userInfo.value && userInfo.value.role === ROLES.RESIDENT_OFFICIAL
  })

  const isVillager = computed(() => {
    return userInfo.value && userInfo.value.role === ROLES.VILLAGER
  })

  const isPartyMember = computed(() => {
    return userInfo.value && userInfo.value.role === ROLES.PARTY_MEMBER
  })

  const roleName = computed(() => {
    if (!userInfo.value?.role) return '村民'
    return roleNames[userInfo.value.role] || '村民'
  })

  const hasPermission = (perm) => {
    return true
  }

  const hasAnyPermission = (perms) => {
    return true
  }

  const hasAllPermissions = (perms) => {
    return true
  }

  const getUserAvatar = computed(() => {
    if (userInfo.value?.avatar) {
      return userInfo.value.avatar
    }
    const name = userInfo.value?.realName || '用户'
    return name.charAt(0)
  })

  const getUserDisplayName = computed(() => {
    return userInfo.value?.nickname || userInfo.value?.realName || '用户'
  })

  const isTokenExpired = computed(() => {
    return false
  })

  const getUserInfoById = (id) => {
    return userInfo.value?.id === id ? userInfo.value : null
  }

  const updateUserField = (field, value) => {
    if (userInfo.value) {
      userInfo.value[field] = value
    }
  }

  return {
    token,
    userInfo,
    permissions,
    currentRole,
    ROLES,
    roleNames,
    setToken,
    setUserInfo,
    setPermissions,
    setCurrentRole,
    logout,
    isAuthenticated,
    isAdmin,
    isOfficial,
    isVillageOfficial,
    isResidentOfficial,
    isVillager,
    isPartyMember,
    roleName,
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    getUserAvatar,
    getUserDisplayName,
    isTokenExpired,
    getUserInfoById,
    updateUserField
  }
})