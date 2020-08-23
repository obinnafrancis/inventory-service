package com.vlad.tech.inventoryservice;

import com.google.gson.Gson;
import com.vlad.tech.inventoryservice.daos.PermissionRepository;
import com.vlad.tech.inventoryservice.daos.RoleRepository;
import com.vlad.tech.inventoryservice.daos.UserRepository;
import com.vlad.tech.inventoryservice.models.dtos.Permission;
import com.vlad.tech.inventoryservice.models.dtos.Role;
import com.vlad.tech.inventoryservice.models.dtos.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
//public class Bootstrap {
public class Bootstrap implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private List<Role> savedRoles;
    private List<Permission> savedPermission;
    private PasswordEncoder encoder;

    public Bootstrap(PasswordEncoder encoder,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        savedPermission = createPermissions();
        savedRoles = createRoles();
        updateAdminPermissions();
        createAdminUser();
    }

    List<Permission> createPermissions() {
        if (!permissionRepository.findAll().isEmpty()) return new ArrayList<>();
        log.info("Creating permissions");
        //Permission Controller
        Permission all = new Permission("CREATE_PERMISSION", "User with this permission can create new permissions");
        Permission getPermission = new Permission("GET_PERMISSION", "User with this permission can view permissions");
        Permission getAllPermission = new Permission("GET_ALL_PERMISSIONS", "User with this permission can view all permissions");
        Permission updatePermission = new Permission("UPDATE_PERMISSION", "User with this permission can update permissions");
        Permission deletePermission = new Permission("DELETE_PERMISSION", "User with this permission can delete permissions");
        //Role Controller
        Permission mapRolePermissions = new Permission("MAP_ROLE_PERMISSIONS", "User with this permission can map permissions to role");
        Permission deleteRole = new Permission("DELETE_ROLE", "User with this permission can delete roles");
        Permission getRole = new Permission("GET_ROLE", "User with this permission can view roles");
        Permission allRoles = new Permission("GET_ALL_ROLES", "User with this permission can view roles");
        Permission updateRole = new Permission("UPDATE_ROLE", "User with this permission can update roles");
        Permission createRole = new Permission("CREATE_ROLE", "User with this permission can create roles");
        //User Permissions
        Permission getUserSession = new Permission("GET_SESSION", "User with this permission can get session");
        Permission getUserRole = new Permission("GET_USER", "User with this permission can get user");
        Permission updateUserRole = new Permission("UPDATE_USER", "User with this permission can update user");
        Permission deleteUserRole = new Permission("DELETE_USER", "User with this permission can delete user");
        //Region Permissions
        Permission createRegion = new Permission("CREATE_REGION", "User with the permission can create a region");
        Permission getRegion = new Permission("GET_REGION", "User with the permission can fetch a region");
        Permission getAllRegion = new Permission("GET_ALL_REGIONS", "User with the permission can fetch all regions");
        Permission updateRegion = new Permission("UPDATE_REGION", "User with the permission can update a region");
        Permission deleteRegion = new Permission("DELETE_REGION", "User with the permission can delete a region");
        //Location Permissions
        Permission createLocation = new Permission("CREATE_LOCATION", "User with the permission can create a location");
        Permission getLocation = new Permission("GET_LOCATION", "User with the permission can fetch a location");
        Permission getAllLocations = new Permission("GET_ALL_LOCATIONS", "User with the permission can fetch all location");
        Permission updateLocation = new Permission("UPDATE_LOCATION", "User with the permission can update a location");
        Permission deleteLocation = new Permission("DELETE_LOCATION", "User with the permission can delete a location");
        //Oem Permissions
        Permission createOem = new Permission("CREATE_OEM", "User with the permission can create a oem");
        Permission getOem = new Permission("GET_OEM", "User with the permission can fetch a oem");
        Permission getAllOem = new Permission("GET_ALL_OEMS", "User with the permission can fetch all oem");
        Permission updateOem = new Permission("UPDATE_OEM", "User with the permission can update a oem");
        Permission deleteOem = new Permission("DELETE_OEM", "User with the permission can delete a oem");
        //Storage Permissions
        Permission createStorage = new Permission("CREATE_STORAGE", "User with the permission can create a storage");
        Permission getStorage = new Permission("GET_STORAGE", "User with the permission can fetch a storage");
        Permission getAllStorage = new Permission("GET_ALL_STORAGES", "User with the permission can fetch all storage");
        Permission updateStorage = new Permission("UPDATE_STORAGE", "User with the permission can update a storage");
        Permission deleteStorage = new Permission("DELETE_STORAGE", "User with the permission can delete a storage");
        //Product Permissions
        Permission createProduct = new Permission("CREATE_PRODUCT", "User with the permission can create a product");
        Permission getProduct= new Permission("GET_PRODUCT", "User with the permission can fetch a product");
        Permission getAllProduct = new Permission("GET_ALL_PRODUCTS", "User with the permission can fetch all product");
        Permission updateProduct = new Permission("UPDATE_PRODUCT", "User with the permission can update a product");
        Permission deleteProduct = new Permission("DELETE_PRODUCT", "User with the permission can delete a product");
        //Project Permissions
        Permission createProject = new Permission("CREATE_PROJECT", "User with the permission can create a Project");
        Permission getProject = new Permission("GET_PROJECT", "User with the permission can fetch a Project");
        Permission getAllProject = new Permission("GET_ALL_PROJECTS", "User with the permission can fetch all Projects");
        Permission updateProject = new Permission("UPDATE_PROJECT", "User with the permission can update a Project");
        Permission deleteProject = new Permission("DELETE_PROJECT", "User with the permission can delete a Project");
        //Inventory Permissions
        Permission createInventory = new Permission("CREATE_INVENTORY", "User with the permission can create a Inventory");
        Permission getInventory= new Permission("GET_INVENTORY", "User with the permission can fetch a Inventory");
        Permission getAllInventory = new Permission("GET_ALL_INVENTORIES", "User with the permission can fetch all Inventories");
        Permission updateInventory = new Permission("UPDATE_INVENTORY", "User with the permission can update a Inventory");
        Permission deleteInventory = new Permission("DELETE_INVENTORY", "User with the permission can delete a Inventory");

        List<Permission> permissions = Stream.of(
                all, getAllPermission, getPermission, updatePermission,
                deletePermission, mapRolePermissions, deleteRole, getRole,
                allRoles, updateRole, createRole, getUserSession,getUserRole,updateUserRole,deleteUserRole,
                createRegion,getRegion,getAllRegion,updateRegion,deleteRegion,
                createLocation,getLocation,getAllLocations,updateLocation,deleteLocation,
                createOem,getOem,getAllOem,updateOem,deleteOem,
                createStorage,getStorage,getAllStorage,updateStorage,deleteStorage,
                createProduct,getProduct,getAllProduct,updateProduct,deleteProduct,
                createProject,getProject,getAllProject,updateProject,deleteProject,
                createInventory,getInventory,getAllInventory,updateInventory,deleteInventory
        ).collect(Collectors.toList());

        log.info("Created permissions");
        return permissionRepository.saveAll(permissions);
    }

    List<Role> createRoles() {
        if (!roleRepository.findAll().isEmpty()) return new ArrayList<>();
        log.info("Creating roles");
        Role guest = new Role("GUEST", "Guest User");
        Role admin = new Role("ADMIN", "THE SUPER USER");
        Role dir = new Role("DIRECTOR", "THE DIRECTOR");
        Role hod = new Role("HEAD_OF_DEPARTMENT", "The Head of Department");
        Role pm = new Role("PROJECT_MANAGER", "The Project Manager");
        Role sm = new Role("STORE_MANAGER", "The Store Manager");
        Role te = new Role("TECHNICIAN", "The Technician");
        Role thirdParty = new Role("3RD_PARTY", "3rd Party User");

        log.info("Created roles");
        List<Role> roles = Stream.of(guest, admin, dir, hod, pm, sm, te, thirdParty).collect(Collectors.toList());
        return roleRepository.saveAll(roles);
    }

    void createAdminUser() {
        if (userRepository.findByUsername("theadmin@gmail.com") != null) return;
        log.info("Creating admin user");
        User user = new User();
        user.setUsername("theadmin@gmail.com");
        user.setUserTag("User_001");
        user.setFirstname("Super");
        user.setMiddlename("Admin");
        user.setLastname("User");
        user.setEmail("theadmin@gmail.com");
        user.setPhoneNumber("2348081234567");
        user.setGender("Female");
        user.setPassword(encoder.encode("Password12"));
        user.setDateCreated(new Date());
        user.setDateModified(new Date());
        Role role = roleRepository.findByName("ADMIN");
        user.setRole(role);
        user.setRoleName(role.getName());
        userRepository.save(user);
        log.info("created admin user");
    }

    void updateAdminPermissions() {
        log.info("updating admin permission");
        savedRoles
                .stream()
                .filter(role -> role.getName().equals("ADMIN"))
                .findFirst()
                .ifPresent(role -> {
                    role.setPermissionList(new HashSet<>(savedPermission));
                    roleRepository.save(role);
                });
    }
}
