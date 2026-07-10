package org.rast3ck.mcrp.core;

import org.rast3ck.mcrp.core.config.ConfigManager;
import org.rast3ck.mcrp.core.config.MCRPConfig;
import org.rast3ck.mcrp.core.data.DataManager;
import org.rast3ck.mcrp.core.data.database.DatabaseManager;
import org.rast3ck.mcrp.core.data.listener.PlayerDataListener;
import org.rast3ck.mcrp.core.data.storage.JsonDataStorage;
import org.rast3ck.mcrp.core.economy.*;
import org.rast3ck.mcrp.core.economy.listener.EconomyPlayerListener;
import org.rast3ck.mcrp.core.economy.storage.AccountStorage;
import org.rast3ck.mcrp.core.economy.storage.TransactionStorage;
import org.rast3ck.mcrp.core.event.EventBus;
import org.rast3ck.mcrp.core.job.JobManager;
import org.rast3ck.mcrp.core.job.storage.JobStorage;
import org.rast3ck.mcrp.core.module.ModuleManager;
import org.rast3ck.mcrp.core.module.ModuleRegistry;
import org.rast3ck.mcrp.core.permission.PermissionManager;
import org.rast3ck.mcrp.core.permission.PermissionStorage;
import org.rast3ck.mcrp.core.region.RegionManager;
import org.rast3ck.mcrp.core.region.action.ActionManager;
import org.rast3ck.mcrp.core.region.action.RegionActionManager;
import org.rast3ck.mcrp.core.region.action.storage.RegionActionStorage;
import org.rast3ck.mcrp.core.region.listener.RegionListener;
import org.rast3ck.mcrp.core.region.storage.RegionStorage;
import org.rast3ck.mcrp.core.region.action.executor.CommandActionExecutor;
import org.rast3ck.mcrp.core.region.action.executor.EconomyActionExecutor;
import org.rast3ck.mcrp.core.region.action.executor.MessageActionExecutor;
import org.rast3ck.mcrp.core.region.action.executor.PermissionActionExecutor;
import org.rast3ck.mcrp.core.task.TaskManager;

public final class MCRPCore {

    private final ModuleManager moduleManager;
    private DataManager dataManager;
    private final ConfigManager configManager;
    private final EventBus eventBus;
    private final EconomyManager economyManager;
    private final CurrencyManager currencyManager;
    private final DatabaseManager databaseManager;
    private final AccountManager accountManager;
    private final TransactionManager transactionManager;
    private final AccountStorage accountStorage;
    private final TransactionStorage transactionStorage;

    private final JobManager jobManager;
    private final JobStorage jobStorage;

    private final PermissionStorage permissionStorage;
    private final PermissionManager permissionManager;

    private final EconomyPlayerListener economyPlayerListener;
    private PlayerDataListener playerDataListener;

    private final RegionStorage regionStorage;
    private final RegionManager regionManager;
    private RegionListener regionListener;
    private final RegionActionManager regionActionManager;
    private final RegionActionStorage regionActionStorage;
    private final ActionManager actionManager;

    private final TaskManager taskManager;

    public MCRPCore() {

        this.moduleManager = new ModuleManager();

        this.configManager = new ConfigManager();

        this.eventBus = new EventBus();

        this.taskManager = new TaskManager();

        // Database
        this.databaseManager = new DatabaseManager();

        // Economy Storage
        this.currencyManager = new CurrencyManager();
        this.accountStorage = new AccountStorage(databaseManager, currencyManager);

        // Economy Managers
        this.accountManager = new AccountManager(accountStorage);
        this.transactionStorage = new TransactionStorage(databaseManager);
        this.transactionManager = new TransactionManager(transactionStorage);
        this.economyManager = new EconomyManager(accountManager, transactionManager, databaseManager);
        this.economyPlayerListener = new EconomyPlayerListener(eventBus, accountManager, currencyManager);

        // Job
        this.jobStorage = new JobStorage(databaseManager);
        this.jobManager = new JobManager(jobStorage);

        // Permission
        this.permissionStorage = new PermissionStorage(databaseManager);
        this.permissionManager = new PermissionManager(jobManager, permissionStorage);

        // Region
        this.regionStorage = new RegionStorage(databaseManager);
        this.regionManager = new RegionManager(regionStorage);
        this.regionActionStorage = new RegionActionStorage(databaseManager);
        this.regionActionManager = new RegionActionManager(regionActionStorage);
        this.actionManager = new ActionManager();
        this.actionManager.register(new MessageActionExecutor());
        this.actionManager.register(new CommandActionExecutor());
        this.actionManager.register(new PermissionActionExecutor());
        this.actionManager.register(new EconomyActionExecutor());
        this.regionListener = new RegionListener(eventBus, regionActionManager, actionManager, permissionManager);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public RegionActionManager getRegionActionManager() {
        return regionActionManager;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public CurrencyManager getCurrencyManager() {
        return currencyManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    private void loadConfigs() {
        configManager.register(new MCRPConfig());
        configManager.loadAll();
    }

    private void loadManagers() {

    }

    private void registerModules() {
        ModuleRegistry.getModules().forEach(moduleManager::register);
    }

    private void loadEconomy() {

        Currency dollar = new Currency("dollar", "Dollar", "$");

        currencyManager.setDefault(dollar);

    }

    private void initializeModules() {
        moduleManager.initializeModules();
    }

    public void initialize(java.nio.file.Path worldDirectory) {

        this.dataManager = new DataManager(new JsonDataStorage(worldDirectory));
        this.playerDataListener = new PlayerDataListener(eventBus, dataManager, jobManager, accountManager);

        // Configuración
        loadConfigs();

        // Database
        databaseManager.connect(worldDirectory);

        // =========================
        // Storage
        // =========================

        // Economy
        accountStorage.createTable();
        transactionStorage.createTable();

        // Jobs
        jobStorage.createTables();

        // Permissions
        permissionStorage.createTables();

        // Regions
        regionStorage.createTables();
        regionActionStorage.createTable();

        // =========================
        // Load Data
        // =========================

        regionManager.load();
        regionActionManager.load();

        // =========================
        // Initialize Systems
        // =========================

        loadEconomy();

        // =========================
        // Modules
        // =========================

        registerModules();

        initializeModules();

    }

}
