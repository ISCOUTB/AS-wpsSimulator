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
package org.wpsim.PeasantFamily.Tasks.L4SkillsResources;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.System.AdmBESA;
import org.wpsim.MarketPlace.Guards.MarketAgentGuard;
import org.wpsim.MarketPlace.Data.MarketMessage;
import org.wpsim.PeasantFamily.Data.PeasantFamilyBDIAgentBelieves;
import org.wpsim.PeasantFamily.Data.Utils.ResourceNeededType;
import org.wpsim.PeasantFamily.Data.Utils.TimeConsumedBy;
import org.wpsim.Simulator.Base.wpsTask;
import org.wpsim.Simulator.wpsStart;
import org.wpsim.Viewer.Data.wpsReport;
import rational.mapping.Believes;

import static org.wpsim.MarketPlace.Data.MarketMessageType.BUY_WATER;

/**
 *
 * @author jairo
 */
public class ObtainWaterTask extends wpsTask {

    /**
     *
     * @param parameters
     */
    @Override
    public void executeTask(Believes parameters) {
        this.setExecuted(false);
        PeasantFamilyBDIAgentBelieves believes = (PeasantFamilyBDIAgentBelieves) parameters;
        believes.addTaskToLog(believes.getInternalCurrentDate());
        wpsReport.info("🚰🚰🚰 Comprando Agua", believes.getPeasantProfile().getPeasantFamilyAlias());
        believes.useTime(TimeConsumedBy.valueOf(this.getClass().getSimpleName()));
        try {
            AdmBESA.getInstance().getHandlerByAlias(
                    wpsStart.config.getMarketAgentName()
            ).sendEvent(
                    new EventBESA(
                            MarketAgentGuard.class.getName(),
                            new MarketMessage(
                                    BUY_WATER,
                                    believes.getPeasantProfile().getPeasantFamilyAlias(),
                                    100,
                                    believes.getInternalCurrentDate()
                            )
                    )
            );
        } catch (ExceptionBESA ex) {
            wpsReport.error(ex, "obtainWaterTask");
        }
        believes.setCurrentResourceNeededType(ResourceNeededType.NONE);
    }

}
