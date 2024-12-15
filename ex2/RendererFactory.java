public class RendererFactory {

    /**
     * The function return an Object of the fit String its get
     * @param renderer
     * @param size
     * @return
     */
    public Renderer buildRenderer(String renderer, int size) {
        if (renderer.equals("console")) {
            return new ConsoleRenderer(size);
        }
        return new VoidRenderer();
    }
}
