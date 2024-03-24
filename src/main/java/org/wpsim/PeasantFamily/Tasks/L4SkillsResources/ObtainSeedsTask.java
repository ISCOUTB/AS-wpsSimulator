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
import org.wpsim.WellProdSim.Base.wpsTask;
import org.wpsim.WellProdSim.wpsStart;
import org.wpsim.MarketPlace.Guards.MarketPlaceGuard;
import org.wpsim.MarketPlace.Data.MarketPlaceMessage;
import org.wpsim.ViewerLens.Util.wpsReport;
import rational.mapping.Believes;
import org.wpsim.PeasantFamily.Data.PeasantFamilyBelieves;
import org.wpsim.PeasantFamily.Data.Utils.ResourceNeededType;
import org.wpsim.PeasantFamily.Data.Utils.TimeConsumedBy;

import static org.wpsim.MarketPlace.Data.MarketPlaceMessageType.BUY_SEEDS;

/**
 *
 * @author jairo
 */
public class ObtainSeedsTask extends wpsTask {

    /**
     *
     * @param parameters
     */
    @Override
    public void executeTask(Believes parameters) {
        this.setExecuted(false);
        //wpsReport.info("⚙️⚙️⚙️");
        PeasantFamilyBelieves believes = (PeasantFamilyBelieves) parameters;
        believes.addTaskToLog(believes.getInternalCurrentDate());
        believes.useTime(TimeConsumedBy.ObtainSeedsTask.getTime());

        // @TODO: Se debe calcular cuanto necesitas prestar hasta que se coseche.
        try {
            AdmBESA.getInstance().getHandlerByAlias(
                    wpsStart.config.getMarketAgentName()
            ).sendEvent(
                    new EventBESA(
                            MarketPlaceGuard.class.getName(),
                            new MarketPlaceMessage(
                                    BUY_SEEDS,
                                    believes.getPeasantProfile().getPeasantFamilyAlias(),
                                    believes.getPeasantProfile().getSeedsNeeded(),
                                    believes.getInternalCurrentDate()
                            )
                    )
            );

        } catch (ExceptionBESA ex) {
            wpsReport.error(ex, "ObtainSeedsTask.executeTask( )");
        }
        believes.setCurrentResourceNeededType(ResourceNeededType.NONE);
    }

}
