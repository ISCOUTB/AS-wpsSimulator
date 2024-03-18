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
package org.wpsim.SimulationControl.Guards;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.AdmBESA;
import org.wpsim.SimulationControl.Data.SimulationControlState;
import org.wpsim.SimulationControl.Data.ControlMessage;
import org.wpsim.PeasantFamily.Guards.FromSimulationControl.ToControlMessage;
import org.wpsim.PeasantFamily.Guards.FromSimulationControl.FromSimulationControlGuard;
import org.wpsim.WellProdSim.wpsStart;
import org.wpsim.ViewerLens.Util.wpsReport;

/**
 *
 * @author jairo
 */
public class AliveAgentGuard extends GuardBESA {
    /**
     *
     * @param event Event rising the Guard
     */
    @Override
    public void funcExecGuard(EventBESA event) {
        ToControlMessage toControlMessage = (ToControlMessage) event.getData();
        SimulationControlState state = (SimulationControlState) this.getAgent().getState();

        //wpsReport.debug(agentAlias + " Alive - " + toControlMessage.getDays(), "ControlAgentGuard");
        //System.out.println(agentAlias + " Alive - " + toControlMessage.getDays() +  " ControlAgentGuard");
        state.addAgentToMap(toControlMessage.getPeasantFamilyAlias(), toControlMessage.getCurrentDay());

        try {
            int count = wpsStart.peasantFamiliesAgents;
            AdmBESA.getInstance().getHandlerByAlias(
                    toControlMessage.getPeasantFamilyAlias()
            ).sendEvent(
                    new EventBESA(
                            FromSimulationControlGuard.class.getName(),
                            new ControlMessage(
                                    toControlMessage.getPeasantFamilyAlias(),
                                    false
                            )
                    )
            );
            //wpsReport.debug("Initial Unblock " + agentAlias + " sent " + count, "ControlAgentState");
            //System.out.println("Initial Unblock " + agentAlias + " sent " + count + " ControlAgentState");
        } catch (ExceptionBESA ex) {
            wpsReport.debug(ex, "ControlAgentState");
        }

    }


}
