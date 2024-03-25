/**
 * ==========================================================================
 * __      __ _ __   ___  *    WellProdSim                                  *
 * \ \ /\ / /| '_ \ / __| *    @version 1.0                                 *
 * \ V  V / | |_) |\__ \ *    @since 2023                                  *
 * \_/\_/  | .__/ |___/ *                                                 *
 * | |          *    @author Jairo Serrano                        *
 * |_|          *    @author Enrique Gonzalez                     *
 * ==========================================================================
 * Social Simulator used to estimate productivity and well-being of peasant *
 * families. It is event oriented, high concurrency, heterogeneous time     *
 * management and emotional reasoning BDI.                                  *
 * ==========================================================================
 */
package org.wpsim.PeasantFamily.Tasks.L1Survival;

import BESA.Emotional.EmotionalEvent;
import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import org.wpsim.BankOffice.Guards.BankOfficeGuard;
import org.wpsim.BankOffice.Data.BankOfficeMessage;
import org.wpsim.SimulationControl.Util.ControlCurrentDate;
import org.wpsim.WellProdSim.Base.wpsTask;
import org.wpsim.WellProdSim.wpsStart;
import org.wpsim.PeasantFamily.Data.PeasantFamilyBelieves;
import org.wpsim.PeasantFamily.Data.Utils.TimeConsumedBy;
import org.wpsim.ViewerLens.Util.wpsReport;
import rational.mapping.Believes;

import static org.wpsim.BankOffice.Data.BankOfficeMessageType.ASK_CURRENT_TERM;

/**
 *
 */
public class DoVitalsTask extends wpsTask {

    /**
     * @param parameters
     */
    @Override
    public void executeTask(Believes parameters) {
        this.setExecuted(false);
        PeasantFamilyBelieves believes = (PeasantFamilyBelieves) parameters;
        believes.setNewDay(false);
        believes.useTime(TimeConsumedBy.DoVitalsTask.getTime());
        if (believes.getPeasantProfile().getMoney()<=500000){
            believes.processEmotionalEvent(new EmotionalEvent("FAMILY", "STARVING", "FOOD"));
        }else {
            believes.processEmotionalEvent(new EmotionalEvent("FAMILY", "DOVITALS", "TIME"));
        }
        // Check debts
        checkBankDebt(believes);
        believes.getPeasantProfile().discountDailyMoney();
        believes.addTaskToLog(believes.getInternalCurrentDate());
    }

    /**
     * Check for the loan pay amount only on first day of month
     *
     * @param believes
     */
    private void checkBankDebt(PeasantFamilyBelieves believes) {
        if (ControlCurrentDate.getInstance().isFirstDayOfMonth(believes.getInternalCurrentDate())
                && believes.getCurrentDay() > 6 && believes.isHaveLoan()) {
            try {
                AdmBESA.getInstance().getHandlerByAlias(
                        wpsStart.config.getBankAgentName()
                ).sendEvent(
                        new EventBESA(
                                BankOfficeGuard.class.getName(),
                                new BankOfficeMessage(
                                        ASK_CURRENT_TERM,
                                        believes.getAlias(),
                                        believes.getInternalCurrentDate()
                                )
                        )
                );
            } catch (ExceptionBESA ex) {
                wpsReport.error(ex, believes.getPeasantProfile().getPeasantFamilyAlias());
            }
        }
        if (believes.getPeasantProfile().getLoanAmountToPay() > believes.getPeasantProfile().getMoney()){
            //System.out.println("No se puede pagar la cuota " + believes.getAlias() + " la cuota es de " + believes.getPeasantProfile().getLoanAmountToPay() + " y tiene " + believes.getPeasantProfile().getMoney());
            believes.processEmotionalEvent(new EmotionalEvent("FAMILY", "UNPAYINGDEBTS", "MONEY"));
        }
    }
}
