package by.march8.ecs.application.modules;

import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.economists.EconomistModuleSet;
import by.march8.ecs.application.modules.marketing.MarketingModuleSet;
import by.march8.ecs.application.modules.nsi.NsiModuleSet;
import by.march8.ecs.application.modules.otk.OtkModuleSet;
import by.march8.ecs.application.modules.sales.SalesModuleSet;
import by.march8.ecs.application.modules.marking.MarkingModuleSet;
import by.march8.ecs.application.modules.packing.PackingModuleSet;
import by.march8.ecs.application.modules.plan.PlanModuleSet;
import by.march8.ecs.application.modules.production.ProductionDeptModuleSet;
import by.march8.ecs.application.modules.references.ReferencesModuleSet;
import by.march8.ecs.application.modules.sewing.SewingModuleSet;
import by.march8.ecs.application.modules.tech.TechModuleSet;
import by.march8.ecs.application.modules.warehouse.WarehouseDeptModuleSet;
import by.march8.ecs.application.modules.wes.WesModuleSet;
import by.march8.ecs.application.shell.administration.SettingsModuleSet;

import java.util.logging.Logger;

/**
 * Класс контроллера модулей. Управляет подключением модулей к программе в
 * зависимости от роли текущей учетной записи.
 */
public class MainModuleController {

    private static final Logger log = Logger.getLogger(MainModuleController.class.getName());
    private Byte count = 0;

    public MainModuleController(MainController controller) {

        try{
            NsiModuleSet nsiModuleSet = new NsiModuleSet();
            nsiModuleSet.initialModuleSet(controller);
            count++;
        }
        catch (Exception e) {
            log.severe("Ошибка инициализации набора модулей НСИ" + e.getMessage());
        }
        try{
            OtkModuleSet otkModuleSet = new OtkModuleSet();
            otkModuleSet.initialModuleSet(controller);
            count++;
        }
        catch(Exception e){
            log.severe("Ошибка инициализации набора модулей ОТК" + e.getMessage());
        }
        try{
            MarketingModuleSet marketingModuleSet = new MarketingModuleSet();
            marketingModuleSet.initialModuleSet(controller);
            count++;
        }
        catch(Exception e){
            log.severe("Ошибка инициализации набора модулей Маркетинг" + e.getMessage());
        }

        try{
            MarkingModuleSet markingModuleSet = new MarkingModuleSet();
            markingModuleSet.initialModuleSet(controller);
            count++;
        }
        catch (Exception e) {
            log.severe("Ошибка инициализации набора модулей Маркировка" + e.getMessage());
        }

        try{
            WesModuleSet wesModuleSet = new WesModuleSet();
            wesModuleSet.initialModuleSet(controller);
            count++;
        }
        catch(Exception e){
            log.severe("Ошибка инициализации набора модулей ВЭС" + e.getMessage());
        }

        try {
            SewingModuleSet sewingModuleSet = new SewingModuleSet();
            sewingModuleSet.initialModuleSet(controller);
            count++;
        }
        catch (Exception e) {
            log.severe("Ошибка инициализации набора модулей Пошив" + e.getMessage());
        }

        try {
            SalesModuleSet salesModuleSetModuleSet = new SalesModuleSet();
            salesModuleSetModuleSet.initialModuleSet(controller);
            count++;
        }
        catch (Exception e) {
            log.severe("Ошибка инициализации набора модулей Сбыт" + e.getMessage());
        }

        try {
            EconomistModuleSet economistModule = new EconomistModuleSet();
            economistModule.initialModuleSet(controller);
            count++;
        }
        catch (Exception e) {
            log.severe("Ошибка инициализации набора модулей Экономисты" + e.getMessage());
        }

        try {
            TechModuleSet techModule = new TechModuleSet();
            techModule.initialModuleSet(controller);
            count++;
        }
        catch (Exception e) {
            log.severe("Ошибка инициализации набора модулей Тех отдел" + e.getMessage());
        }

        try {
            ReferencesModuleSet referenceModule = new ReferencesModuleSet();
            referenceModule.initialModuleSet(controller);
            count++;
        }
        catch (Exception e) {
            log.severe("Ошибка инициализации набора модулей Справочник" + e.getMessage());
        }

        try {
            SettingsModuleSet settingsModule = new SettingsModuleSet();
            settingsModule.initialModuleSet(controller);
            count++;
        }
        catch (Exception e) {
            log.severe("Ошибка инициализации набора модулей Администратор" + e.getMessage());
        }

        try {
            WarehouseDeptModuleSet warehouseModuleSet = new WarehouseDeptModuleSet();
            warehouseModuleSet.initialModuleSet(controller);
            count++;
        }
        catch (Exception e) {
            log.severe("Ошибка инициализации набора модулей Склад" + e.getMessage());
        }

        try {
            PackingModuleSet packingModuleSet = new PackingModuleSet();
            packingModuleSet.initialModuleSet(controller);
            count++;
        }
        catch (Exception e) {
            log.severe("Ошибка инициализации набора модулей Упаковка" + e.getMessage());
        }

        try {
            PlanModuleSet planModuleSet = new PlanModuleSet();
            planModuleSet.initialModuleSet(controller);
            count++;
        }
        catch (Exception e) {
            log.severe("Ошибка инициализации набора модулей План" + e.getMessage());
        }

        try {
            ProductionDeptModuleSet productionDeptModule = new ProductionDeptModuleSet();
            productionDeptModule.initialModuleSet(controller);
            count++;
        }
        catch (Exception e) {
            log.severe("Ошибка инициализации набора модулей Производство" + e.getMessage());
        }
    }

    public int getModuleAmount() {
        return count;
    }
}
